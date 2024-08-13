package com.mindzone.service.impl;

import com.mindzone.dto.request.TherapyRequest;
import com.mindzone.dto.response.TherapyResponse;
import com.mindzone.enums.Role;
import com.mindzone.enums.TherapyStatus;
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
import java.util.List;
import java.util.stream.Collectors;

import static com.mindzone.constants.MailsBody.therapyRequestMail;
import static com.mindzone.exception.ExceptionMessages.*;
import static com.mindzone.util.WeekDayScheduleUtil.fitsIn;
import static com.mindzone.util.WeekDayScheduleUtil.overlaps;

@Service
@AllArgsConstructor
public class TherapyServiceImpl implements TherapyService {

    private TherapyRepository therapyRepository;
    private UserService userService;
    private MailService mailService;
    private UltimateModelMapper m;

    @Override
    public TherapyResponse requestTherapy(TherapyRequest therapyRequest, User user) {
        // TODO cancelar request de terapia
        // TODO listar minhas requests
        List<Therapy> patientTherapies = therapyRepository.findAllByPatientIdAndTherapyStatus(user.getId(), TherapyStatus.APPROVED);
        User requestedProfessional = userService.getById(therapyRequest.getProfessionalId());
        ProfessionalInfo requestedProfessionalData = requestedProfessional.getProfessionalInfo();

        // Checking if the modality requested is accepted by the professional
        if (!requestedProfessionalData.getTherapyModalities().contains(therapyRequest.getTherapyModality())) {
            throw new ApiRequestException(THERAPY_MODALITY_NOT_ACCEPTED);
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

        // initialize and save request
        Therapy therapy = m.map(therapyRequest, Therapy.class);
        therapy.setPatientId(user.getId());
        therapy.setTherapyStatus(TherapyStatus.PENDING);
        therapy.setCompletedSessions(new ArrayList<>());
        therapyRepository.save(therapy);

        // notify requested professional
        mailService.sendMail(therapyRequestMail(
                requestedProfessional.getEmail(),
                user.getName()
        ));

        return m.map(therapy, TherapyResponse.class);
    }

    @Override
    public TherapyResponse get(User user, String id) {
        Therapy therapy = getById(id);

        if (user.getRole() == Role.PATIENT) {
          if (!therapy.getPatientId().equals(user.getId())) {
              throw new ApiRequestException(USER_UNAUTHORIZED);
          }
        } else if (user.getRole() == Role.PROFESSIONAL) {
            List<Therapy> patientTherapies = therapyRepository.findAllByPatientId(user.getId());
            List<Therapy> patientTherapiesWithLoggedProfessional = patientTherapies.stream().filter(t -> t.getProfessionalId().equals(user.getId())).toList();
            if (patientTherapiesWithLoggedProfessional.isEmpty()) {
                throw new ApiRequestException(USER_UNAUTHORIZED);
            }
        }
        return m.map(therapy, TherapyResponse.class);
    }

    private Therapy getById(String id) {
        return therapyRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(THERAPY_NOT_FOUND));
    }
}
