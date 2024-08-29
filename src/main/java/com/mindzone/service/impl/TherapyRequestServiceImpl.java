package com.mindzone.service.impl;

import com.mindzone.dto.request.TherapyRequest;
import com.mindzone.dto.request.TherapyRequestAnalysis;
import com.mindzone.dto.response.TherapyResponse;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.therapy.Session;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.ProfessionalInfo;
import com.mindzone.model.user.User;
import com.mindzone.model.user.WeekDaySchedule;
import com.mindzone.repository.SessionRepository;
import com.mindzone.repository.TherapyRepository;
import com.mindzone.service.interfaces.MailService;
import com.mindzone.service.interfaces.TherapyRequestService;
import com.mindzone.service.interfaces.TherapyService;
import com.mindzone.service.interfaces.UserService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mindzone.constants.MailsBody.*;
import static com.mindzone.enums.TherapyStatus.*;
import static com.mindzone.exception.ExceptionMessages.*;
import static com.mindzone.util.WeekDayScheduleUtil.*;

@Service
@AllArgsConstructor
public class TherapyRequestServiceImpl implements TherapyRequestService {

    private TherapyRepository therapyRepository;
    private SessionRepository sessionRepository;
    private TherapyService t;
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
        t.save(therapy);

        // notify requested professional
        mailService.sendMail(therapyRequestMail(
                userService.getById(therapyRequest.getProfessionalId()).getEmail(),
                patient.getName()
        ));

        return m.map(therapy, TherapyResponse.class);
    }

    @Override
    public TherapyResponse updateRequest(User patient, String id, TherapyRequest therapyRequest) {
        Therapy therapy = t.getById(id);
        t.canManage(patient, therapy);
        if (therapy.getTherapyStatus() != PENDING) {
            throw new ApiRequestException(THERAPY_IS_NOT_EDITABLE);
        }
        checkTherapyRequestValidation(therapyRequest, patient);
        m.map(therapyRequest, therapy);
        t.save(therapy);
        return m.map(therapy, TherapyResponse.class);
    }

    @Override
    public TherapyResponse deleteRequest(User patient, String id) {
        Therapy therapy = t.getById(id);
        t.canManage(patient, therapy);
        if (therapy.getTherapyStatus() != PENDING) {
            throw new ApiRequestException(THERAPY_IS_NOT_EDITABLE);
        }
        therapyRepository.delete(therapy);
        return m.map(therapy, TherapyResponse.class);
    }

    @Override
    public TherapyResponse analyseRequest(User professional, String id, TherapyRequestAnalysis analysis) {
        Therapy therapy = t.getById(id);
        User patient = userService.getById(therapy.getPatientId());
        t.canManage(professional, therapy);

        if (analysis.getStatus() == DENIED) {
            // notify patient about professional final analysis
            mailService.sendMail(
                    deniedTherapyRequestMail(patient.getEmail(), professional.getName(), analysis.getDenialJustification())
            );
            therapy.setTherapyStatus(analysis.getStatus());
            t.save(therapy);

        } else if (analysis.getStatus() == APPROVED) {
            // Automatically cancelling all therapy requests to the current professional that overlaps the therapy in analysis
            List<Therapy> professionalPendingTherapies = therapyRepository.findAllByProfessionalIdAndTherapyStatus(professional.getId(), PENDING);
            for (Therapy pendingTherapy : professionalPendingTherapies) {
                if (overlaps(therapy.getSchedule(), pendingTherapy.getSchedule())) {
                    User patientNotified = userService.getById(pendingTherapy.getPatientId());
                    pendingTherapy.setTherapyStatus(CANCELED);
                    t.save(pendingTherapy);
                    // notify patient about this occurence
                    mailService.sendMail(canceledTherapyRequestMail(patientNotified.getEmail(), professional.getName()));
                }
            }

            // Automatically cancelling all therapy requests that the patient made which overlaps the current therapy in analysis
            List<Therapy> patientPendingTherapies = therapyRepository.findAllByPatientIdAndTherapyStatus(patient.getId(), PENDING);
            for (Therapy pendingTherapy : patientPendingTherapies) {
                if (overlaps(therapy.getSchedule(), pendingTherapy.getSchedule())) {
                    pendingTherapy.setTherapyStatus(CANCELED);
                    t.save(pendingTherapy);
                    patientPendingTherapies.remove(pendingTherapy); // this list will be used once more below
                }
            }

            // Automatically cancelling all therapy requests that the patient made to other professionals with the same profession
            List<Therapy> sameProfessionPatientPendingTherapies = patientPendingTherapies.stream().filter(
                    t -> userService.get(t.getProfessionalId()).getProfessionalInfo().getProfession() == professional.getProfessionalInfo().getProfession()
            ).toList();
            for (Therapy th : sameProfessionPatientPendingTherapies) {
                th.setTherapyStatus(CANCELED);
                t.save(th);
            }

            // Updates professional availability based on the current therapy in analysis
            removeFrom(professional.getProfessionalInfo().getAvailability(), therapy.getSchedule());
            userService.save(professional);

            // update and save
            therapy.setTherapyStatus(analysis.getStatus());
            therapy.setSince(new Date());
            therapy.setUrl(analysis.getApprovalSessionsUrl());
            therapy.setActive(true);

            Session session = Session.builder()
                    .therapyId(id)
                    .date(getNextOccurrence(therapy.getSchedule(), new Date()))
                    .build();
            Date now = new Date();
            session.setCreatedAt(now);
            session.setUpdatedAt(now);
            sessionRepository.save(session);
            therapy.setNextSessionId(session.getId());
            t.save(therapy);

            // notify patient about professional final analysis
            mailService.sendMail(
                    approvedTherapyRequestMail(patient.getEmail(), professional.getName())
            );
        }

        return m.map(therapy, TherapyResponse.class);
    }

    private void checkTherapyRequestValidation(TherapyRequest therapyRequest, User patient) {
        User requestedProfessional = userService.getById(therapyRequest.getProfessionalId());
        ProfessionalInfo info = requestedProfessional.getProfessionalInfo();

        List<Therapy> patientTherapies = therapyRepository.findAllByProfessionalIdAndActiveIsTrue(patient.getId());
        for (Therapy patientTherapy : patientTherapies) {
            // Checking if patient is already in therapy with the profession requested
            User activeProfessional = userService.getById(patientTherapy.getProfessionalId());
            if (activeProfessional.getProfessionalInfo().getProfession() == info.getProfession()) {
                throw new ApiRequestException(PATIENT_IS_ALREADY_IN_THERAPY_WITH_THIS_PROFESSION);
            }

            // Checking if the patient already has therapy
            // in a schedule that overlaps the requested current therapy
            if (overlaps(therapyRequest.getSchedule(), patientTherapy.getSchedule())) {
                throw new ApiRequestException(THERAPY_TIME_CONFLICT);
            }
        }

        // Checking if the requested therapy schedule fits in the professional availability
        if (!fitsIn(info.getAvailability(), therapyRequest.getSchedule())) {
            throw new ApiRequestException(THERAPY_TIME_CONFLICT);
        }
    }
}
