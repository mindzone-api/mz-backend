package com.mindzone.service.impl;

import com.mindzone.dto.request.TherapyToValidate;
import com.mindzone.dto.request.TherapyUpdate;
import com.mindzone.dto.response.TherapyResponse;
import com.mindzone.dto.response.listed.ListedTherapy;
import com.mindzone.enums.HealthPlan;
import com.mindzone.enums.PaymentMethod;
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

import java.util.Date;
import java.util.List;

import static com.mindzone.constants.MailsBody.deniedTherapyRequestMail;
import static com.mindzone.constants.MailsBody.therapyUpdateMail;
import static com.mindzone.enums.Role.PATIENT;
import static com.mindzone.enums.Role.PROFESSIONAL;
import static com.mindzone.enums.TherapyStatus.APPROVED;
import static com.mindzone.exception.ExceptionMessages.*;
import static com.mindzone.util.WeekDayScheduleUtil.*;

@Service
@AllArgsConstructor
public class TherapyServiceImpl implements TherapyService {

    private TherapyRepository therapyRepository;
    private UserService userService;
    private UltimateModelMapper m;
    private MailService mailService;

    public void save(Therapy therapy) {
        therapy.setUpdatedAt(new Date());
        therapyRepository.save(therapy);
    }

    public Therapy getById(String id) {
        return therapyRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(THERAPY_NOT_FOUND));
    }

    public void canAccess(User user, Therapy therapy) {
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

    public void canManage(User user, Therapy therapy) {
        if (user.getRole() == PATIENT && !therapy.getPatientId().equals(user.getId())) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
        } else if (user.getRole() == PROFESSIONAL && !therapy.getProfessionalId().equals(user.getId())) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
        }
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
    public TherapyResponse update(User professional, String id, TherapyUpdate therapyUpdate) {
        Therapy therapy = getById(id);
        canManage(professional, therapy);
        if (therapy.getTherapyStatus() != APPROVED) {
            throw new ApiRequestException(THERAPY_IS_NOT_EDITABLE);
        }
        checkTherapyValidation(therapyUpdate, professional.getProfessionalInfo());
        professional.getProfessionalInfo().setAvailability(updateProfessionalSchedule(professional, therapy.getSchedule(), therapyUpdate.getSchedule()));
        m.map(therapyUpdate, therapy);
        therapy.setSchedule(therapyUpdate.getSchedule());
        therapy.setNextSession(getNextOccurence(therapy.getSchedule()));
        userService.save(professional);
        save(therapy);

        // notify patient about professional final analysis
        mailService.sendMail(
                therapyUpdateMail(userService.getById(therapy.getPatientId()).getEmail(), professional.getName())
        );
        return m.map(therapy, TherapyResponse.class);
    }

    // Used in therapy update. The oldSchedule will be added back and the new one will be removed from his agenda
    private List<WeekDaySchedule> updateProfessionalSchedule(User professional, List<WeekDaySchedule> oldSchedule, List<WeekDaySchedule> newSchedule) {
        List<WeekDaySchedule> scheduleWithoutOld = mergeWith(professional.getProfessionalInfo().getAvailability(), oldSchedule);
        return removeFrom(scheduleWithoutOld, newSchedule);
    }

    public void checkTherapyValidation(TherapyToValidate ttv, ProfessionalInfo info) {
        // Checking if the modality requested is accepted by the professional
        if (!info.getTherapyModalities().contains(ttv.getTherapyModality())) {
            throw new ApiRequestException(THERAPY_MODALITY_NOT_ACCEPTED);
        }

        // Checking if the payment method and health plan were filled exclusively (XOR)
        boolean hasPayment = ttv.getPaymentMethod() != null;
        boolean hasHealthPlan = ttv.getHealthPlan() != null;
        if (hasPayment == hasHealthPlan) {
            throw new ApiRequestException(INVALID_PAYMENT_AND_HEALTH_PLAN_FIELDS);
        }

        // Checking if the payment method is accepted by the professional
        List<PaymentMethod> paymentMethods = info.getPaymentMethods();
        if (hasPayment && !paymentMethods.isEmpty() && !paymentMethods.contains(ttv.getPaymentMethod())) {
            throw new ApiRequestException(PAYMENT_METHOD_NOT_ACCEPTED);
        }

        // Checking if the health plan is accepted by the professional
        List<HealthPlan> healthPlans = info.getAcceptedHealthPlans();
        if (hasHealthPlan && !healthPlans.isEmpty() && !healthPlans.contains(ttv.getHealthPlan())) {
            throw new ApiRequestException(HEALTH_PLAN_NOT_ACCEPTED);
        }

        // Checking if the amount of time requested fits in the requested professional SessionDuration
        long professionalSessionDuration = info.getSessionDuration().toMinutes();
        for (WeekDaySchedule weekDaySchedule : ttv.getSchedule()) {
            for (TimeRange timeRange : weekDaySchedule.getDaySchedule()) {
                long requestedSessionDuration = timeRange.getEndsAt() - timeRange.getStartsAt();
                if (requestedSessionDuration % professionalSessionDuration != 0) {
                    throw new ApiRequestException(INVALID_SESSION_DURATION);
                }
            }
        }

        // Checking if the requested therapy schedule fits in the professional availability
        if (!fitsIn(info.getAvailability(), ttv.getSchedule())) {
            throw new ApiRequestException(INVALID_THERAPY_SCHEDULE);
        }
    }
}
