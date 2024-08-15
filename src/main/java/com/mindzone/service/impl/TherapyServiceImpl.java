package com.mindzone.service.impl;

import com.mindzone.dto.request.TherapyRequest;
import com.mindzone.dto.request.TherapyRequestAnalysis;
import com.mindzone.dto.response.TherapyResponse;
import com.mindzone.dto.response.listed.ListedTherapy;
import com.mindzone.enums.HealthPlan;
import com.mindzone.enums.PaymentMethod;
import com.mindzone.enums.TherapyModality;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.ProfessionalInfo;
import com.mindzone.model.user.TimeRange;
import com.mindzone.model.user.User;
import com.mindzone.model.user.WeekDaySchedule;
import com.mindzone.repository.TherapyRepository;
import com.mindzone.service.interfaces.MailService;
import com.mindzone.service.interfaces.TherapyService;
import com.mindzone.service.interfaces.UserService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mindzone.constants.MailsBody.*;
import static com.mindzone.enums.Role.PATIENT;
import static com.mindzone.enums.Role.PROFESSIONAL;
import static com.mindzone.enums.TherapyModality.ONLINE;
import static com.mindzone.enums.TherapyStatus.*;
import static com.mindzone.exception.ExceptionMessages.*;
import static com.mindzone.util.WeekDayScheduleUtil.*;

@Service
@AllArgsConstructor
public class TherapyServiceImpl implements TherapyService {

    private TherapyRepository therapyRepository;
    private UserService userService;
    private MailService mailService;
    private UltimateModelMapper m;

    @Override
    public TherapyResponse requestTherapy(TherapyRequest therapyRequest, User patient) {
        // Checking if patient already has a valid request with this professional
        // This check is not in the function below because it is also used on update therapy
        for (Therapy t : therapyRepository.findAllByPatientIdAndProfessionalId(patient.getId(), therapyRequest.getProfessionalId())) {
            if (t.getTherapyStatus() == APPROVED || t.getTherapyStatus() == PENDING) {
                throw new ApiRequestException(THERAPY_ALREADY_EXISTS);
            }
        }
        checkTherapyRequestValidation(therapyRequest, patient);

        // initialize and save request
        Therapy therapy = m.map(therapyRequest, Therapy.class);
        therapy.setPatientId(patient.getId());
        therapy.setTherapyStatus(PENDING);
        therapy.setActive(false);
        therapy.setCreatedAt(new Date());
        save(therapy);

        // notify requested professional
        mailService.sendMail(therapyRequestMail(
                userService.getById(therapyRequest.getProfessionalId()).getEmail(),
                patient.getName()
        ));

        return m.map(therapy, TherapyResponse.class);
    }

    @Override
    public TherapyResponse get(User user, String id) {
        Therapy therapy = getById(id);
        canAccess(user, therapy);
        return m.map(therapy, TherapyResponse.class);
    }

    @Override
    public List<ListedTherapy> getAll(User user) {
        return (
                user.getRole() == PATIENT ?
                        m.mapToList(therapyRepository.findAllByPatientId(user.getId()), ListedTherapy.class) :
                        m.mapToList(therapyRepository.findAllByProfessionalId(user.getId()), ListedTherapy.class)
        );
    }

    @Override
    public TherapyResponse updateRequest(User patient, String id, TherapyRequest therapyRequest) {
        Therapy therapy = getById(id);
        canManage(patient, therapy);
        if (therapy.getTherapyStatus() != PENDING) {
            throw new ApiRequestException(THERAPY_IS_NOT_EDITABLE_ANYMORE);
        }
        checkTherapyRequestValidation(therapyRequest, patient);
        m.map(therapyRequest, therapy);
        save(therapy);
        return m.map(therapy, TherapyResponse.class);
    }

    @Override
    public TherapyResponse analyseRequest(User professional, String id, TherapyRequestAnalysis analysis) {
        Therapy therapy = getById(id);
        User patient = userService.getById(therapy.getPatientId());
        canManage(professional, therapy);

        // Checking if the analysis is valid
        if (analysis.getStatus() == PENDING || therapy.getTherapyStatus() == DENIED) {
            throw new ApiRequestException(INVALID_THERAPY_REQUEST_ANALYSIS);
        }

        // Checking if the denial was sent with a justification text
        if (analysis.getStatus() == DENIED) {
            if (analysis.getDenialJustification() == null || analysis.getDenialJustification().isBlank()) {
                throw new ApiRequestException(MISSING_DENIAL_JUSTIFICATION);
            }
            // notify patient about professional final analysis
            mailService.sendMail(
                    deniedTherapyRequestMail(patient.getEmail(), professional.getName(), analysis.getDenialJustification())
            );
            therapy.setTherapyStatus(analysis.getStatus());
            save(therapy);

        } else if (analysis.getStatus() == APPROVED) {
            if (therapy.getTherapyModality() == ONLINE && (analysis.getApprovalSessionsUrl() == null || analysis.getApprovalSessionsUrl().isBlank())) {
                throw new ApiRequestException(MISSING_SESSIONS_URL);
            }
            // Automatically cancelling all therapy requests to the current professional that overlaps the therapy in analysis
            List<Therapy> professionalPendingTherapies = therapyRepository.findAllByProfessionalIdAndTherapyStatus(professional.getId(), PENDING);
            for (Therapy pendingTherapy : professionalPendingTherapies) {
                if (overlaps(therapy.getSchedule(), pendingTherapy.getSchedule())) {
                    User patientNotified = userService.getById(pendingTherapy.getPatientId());
                    pendingTherapy.setTherapyStatus(CANCELED);
                    save(pendingTherapy);
                    // notify patient about this occurence
                    mailService.sendMail(canceledTherapyRequestMail(patientNotified.getEmail(), professional.getName()));
                }
            }

            // Automatically cancelling all therapy requests that the patient made which overlaps the current therapy in analysis
            List<Therapy> patientPendingTherapies = therapyRepository.findAllByPatientIdAndTherapyStatus(patient.getId(), PENDING);
            for (Therapy pendingTherapy : patientPendingTherapies) {
                if (overlaps(therapy.getSchedule(), pendingTherapy.getSchedule())) {
                    pendingTherapy.setTherapyStatus(CANCELED);
                    save(pendingTherapy);
                    patientPendingTherapies.remove(pendingTherapy); // this list will be used once more below
                }
            }

            // Automatically cancelling all therapy requests that the patient made to other professionals with the same profession
            List<Therapy> sameProfessionPatientPendingTherapies = patientPendingTherapies.stream().filter(
                    t -> userService.get(t.getProfessionalId()).getProfessionalInfo().getProfession() == professional.getProfessionalInfo().getProfession()
            ).toList();
            for (Therapy t : sameProfessionPatientPendingTherapies) {
                t.setTherapyStatus(CANCELED);
                save(t);
            }

            // Updates professional availability based on the current therapy in analysis
            List<WeekDaySchedule> updatedAvailability = removeFrom(professional.getProfessionalInfo().getAvailability(), therapy.getSchedule());
            professional.getProfessionalInfo().setAvailability(updatedAvailability);
            userService.save(professional);

            // update and save
            therapy.setTherapyStatus(analysis.getStatus());
            therapy.setSince(new Date());
            therapy.setUrl(analysis.getApprovalSessionsUrl());
            therapy.setActive(true);
            therapy.setNextSession(getNextOccurence(therapy.getSchedule()));
            therapy.setCompletedSessions(new ArrayList<>());
            save(therapy);

            // notify patient about professional final analysis
            mailService.sendMail(
                    approvedTherapyRequestMail(patient.getEmail(), professional.getName())
            );
        }

        return m.map(therapy, TherapyResponse.class);
    }

    private void save(Therapy therapy) {
        therapy.setUpdatedAt(new Date());
        therapyRepository.save(therapy);
    }

    private Therapy getById(String id) {
        return therapyRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(THERAPY_NOT_FOUND));
    }

    private void checkTherapyRequestValidation(TherapyRequest therapyRequest, User user) {
        User requestedProfessional = userService.getById(therapyRequest.getProfessionalId());
        ProfessionalInfo requestedProfessionalData = requestedProfessional.getProfessionalInfo();

        // Checking if the modality requested is accepted by the professional
        if (!requestedProfessionalData.getTherapyModalities().contains(therapyRequest.getTherapyModality())) {
            throw new ApiRequestException(THERAPY_MODALITY_NOT_ACCEPTED);
        }

        // Checking if the payment method and health plan were filled exclusively (XOR)
        if ((therapyRequest.getPaymentMethod() == null) == (therapyRequest.getHealthPlan() == null)) {
            throw new ApiRequestException(INVALID_PAYMENT_AND_HEALTH_PLAN_FIELDS);
        }

        // Checking if the payment method is accepted by the professional
        List<PaymentMethod> paymentMethods = requestedProfessionalData.getPaymentMethods();
        if (therapyRequest.getPaymentMethod() != null && !paymentMethods.isEmpty() && !paymentMethods.contains(therapyRequest.getPaymentMethod())) {
            throw new ApiRequestException(PAYMENT_METHOD_NOT_ACCEPTED);
        }

        // Checking if the health plan is accepted by the professional
        List<HealthPlan> healthPlans = requestedProfessionalData.getAcceptedHealthPlans();
        if (therapyRequest.getHealthPlan() != null && !healthPlans.isEmpty() && !healthPlans.contains(therapyRequest.getHealthPlan())) {
            throw new ApiRequestException(HEALTH_PLAN_NOT_ACCEPTED);
        }

        // Checking if the amount of time requested fits in the requested professional SessionDuration
        long professionalSessionDuration = requestedProfessionalData.getSessionDuration().toMinutes();
        for (WeekDaySchedule weekDaySchedule : therapyRequest.getSchedule()) {
            for (TimeRange timeRange : weekDaySchedule.getDaySchedule()) {
                long requestedSessionDuration = timeRange.getEndsAt() - timeRange.getStartsAt();
                if (requestedSessionDuration % professionalSessionDuration != 0) {
                    throw new ApiRequestException(INVALID_SESSION_DURATION_REQUEST);
                }
            }
        }

        List<Therapy> patientTherapies = therapyRepository.findAllByPatientIdAndTherapyStatus(user.getId(), APPROVED);
        for (Therapy patientTherapy : patientTherapies) {
            // Checking if patient is already in therapy with the profession requested
            User activeProfessional = userService.getById(patientTherapy.getProfessionalId());
            if (activeProfessional.getProfessionalInfo().getProfession() == requestedProfessionalData.getProfession()) {
                throw new ApiRequestException(PATIENT_IS_ALREADY_IN_THERAPY_WITH_THIS_PROFESSION);
            }

            // Checking if the patient already has therapy
            // in a schedule that overlaps the requested current therapy
            if (overlaps(therapyRequest.getSchedule(), patientTherapy.getSchedule())) {
                throw new ApiRequestException(THERAPY_TIME_CONFLICT);
            }
        }

        // Checking if the requested therapy schedule fits in the professional availability
        if (!fitsIn(requestedProfessionalData.getAvailability(), therapyRequest.getSchedule())) {
            throw new ApiRequestException(INVALID_THERAPY_SCHEDULE);
        }
    }

    private void canAccess(User user, Therapy therapy) {
        if (user.getRole() == PATIENT) {
            if (!therapy.getPatientId().equals(user.getId())) {
                throw new ApiRequestException(USER_UNAUTHORIZED);
            }
        } else if (user.getRole() == PROFESSIONAL) {
            List<Therapy> patientTherapies = therapyRepository.findAllByPatientIdAndTherapyStatus(therapy.getPatientId(), APPROVED);
            List<Therapy> patientTherapiesWithLoggedProfessional = patientTherapies.stream().filter(t -> t.getProfessionalId().equals(user.getId())).toList();
            if (patientTherapiesWithLoggedProfessional.isEmpty()) {
                throw new ApiRequestException(USER_UNAUTHORIZED);
            }
        }
    }

    private void canManage(User user, Therapy therapy) {
        if (user.getRole() == PATIENT && !therapy.getPatientId().equals(user.getId())) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
        } else if (user.getRole() == PROFESSIONAL && !therapy.getProfessionalId().equals(user.getId())) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
        }
    }
}
