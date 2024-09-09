package com.mindzone.service.impl;

import com.mindzone.dto.request.*;
import com.mindzone.dto.response.PatientSessionResponse;
import com.mindzone.dto.response.ProfessionalSessionResponse;
import com.mindzone.dto.response.SessionResponse;
import com.mindzone.dto.response.TherapyResponse;
import com.mindzone.dto.response.listed.ListedSession;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.therapy.Session;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.User;
import com.mindzone.model.user.WeekDaySchedule;
import com.mindzone.repository.SessionRepository;
import com.mindzone.service.interfaces.*;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.Date;
import java.util.List;

import static com.mindzone.constants.MailsBody.sessionsScheduleUpdateMail;
import static com.mindzone.enums.FileType.*;
import static com.mindzone.enums.Role.PATIENT;
import static com.mindzone.exception.ExceptionMessages.*;
import static com.mindzone.util.DateUtil.MILLIS_IN_A_DAY;
import static com.mindzone.util.WeekDayScheduleUtil.*;

@AllArgsConstructor
@Service
public class SessionServiceImpl implements SessionService {

    private TherapyService therapyService;
    private SessionRepository sessionRepository;
    private UserService userService;
    private MailService mailService;
    private SessionFileService sessionFileService;
    private UltimateModelMapper m;

    private Session getById(String id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(SESSION_NOT_FOUND));
    }

    @Override
    public void save(Session model) {
        model.updateDates();
        sessionRepository.save(model);
    }

    @Override
    public void canAccessSessions(User user, Therapy therapy) {
        if (!therapy.getPatientId().equals(user.getId()) && !therapy.getProfessionalId().equals(user.getId())) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
        }
    }

    @Override
    public void canManageSessions(User user, Therapy therapy) {
        if (!therapy.getProfessionalId().equals(user.getId())) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
        }
    }

    @Override
    public Page<ListedSession> getAll(User user, String therapyId, MzPageRequest mzPageRequest) {
        Therapy therapy = therapyService.getById(therapyId);
        canAccessSessions(user, therapy);
        therapyService.isApproved(therapy);
        insertCompletedSessions(therapy);

        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(mzPageRequest.getPage(), mzPageRequest.getSize(), sort);
        Page<Session> sessions = sessionRepository.findByTherapyId(therapyId, pageable);

        return m.pageMap(sessions, ListedSession.class);
    }

    @Override
    public TherapyResponse updateSchedule(User professional, String therapyId, TherapyScheduleUpdate update) {
        Therapy therapy = therapyService.getById(therapyId);
        canManageSessions(professional, therapy);
        therapyService.isActive(therapy);
        insertCompletedSessions(therapy);

        if (!update.getSchedule().equals(therapy.getSchedule())) {
            professional.getProfessionalInfo().setAvailability(
                    updateProfessionalSchedule(
                            professional,
                            therapy.getSchedule(),
                            update.getSchedule()
                    )
            );
            m.map(update, therapy);
        }

        Session nextSession = getById(therapy.getNextSessionId());
        if (update.getNextSessionDate() == null) {
            nextSession.setDate(getNextOccurrence(therapy.getSchedule(), new Date()));
        } else {
            validateNextSessionUpdateRequest(update, nextSession);
            nextSession.setDate(update.getNextSessionDate());
        }

        // notify patient about schedule update
        mailService.sendMail(
                sessionsScheduleUpdateMail(userService.getById(therapy.getPatientId()).getEmail(), professional.getName())
        );
        save(nextSession);
        therapyService.save(therapy);
        userService.save(professional);
        return m.map(therapy, TherapyResponse.class);
    }

    @Override
    public SessionResponse get(User user, String sessionId) {
        Session session = getById(sessionId);
        SessionResponse response;
        Therapy therapy = therapyService.getById(session.getTherapyId());
        canAccessSessions(user, therapy);
        therapyService.isApproved(therapy);


        if (user.getRole() == PATIENT) {
            PatientSessionResponse patientResponse = new PatientSessionResponse();
            patientResponse.setPatientAttatchments(sessionFileService.getBySessionIdAndFileType(sessionId, FOR_PATIENT));
            response = patientResponse;
        } else {
            ProfessionalSessionResponse professionalResponse = new ProfessionalSessionResponse();
            professionalResponse.setProfessionalAttatchments(sessionFileService.getBySessionIdAndFileType(sessionId, FOR_PROFESSIONAL));
            if (userService.isAlly(user, therapy)) {
                professionalResponse.setProfessionalAttatchments(
                    professionalResponse.getProfessionalAttatchments().stream().filter(
                            attachment -> !attachment.getIsMedicalRecord()
                    ).toList()
                );
            }
            response = professionalResponse;
        }
        response.setSharedAttatchments(sessionFileService.getBySessionIdAndFileType(sessionId, SHARED));
        m.map(session, response);
        /*
            TODO
           1- questionnaires statistics -> An endpoint will be built to calculate statistics based on requested and sessions date
           2- homework state -> get it from the creation date
        */
        return response;
    }

    @Override
    public SessionResponse update(User user, String sessionId, SessionRequest request) {
        Session session = getById(sessionId);
        SessionResponse response = null;
        Therapy therapy = therapyService.getById(session.getTherapyId());
        canManageSessions(user, therapy);
        therapyService.isActive(therapy);
        m.map(request, session);


        if (request instanceof PatientSessionRequest patientRequest) {
            PatientSessionResponse patientResponse = new PatientSessionResponse();
            patientResponse.setPatientAttatchments(
                sessionFileService.updateSessionFiles(sessionId, patientRequest.getPatientAttatchments(), FOR_PATIENT)
            );
            response = patientResponse;

        } else if (request instanceof ProfessionalSessionRequest professionalRequest) {
            ProfessionalSessionResponse professionalResponse = new ProfessionalSessionResponse();
            professionalResponse.setProfessionalAttatchments(
                sessionFileService.updateSessionFiles(sessionId, professionalRequest.getProfessionalAttatchments(), FOR_PROFESSIONAL)
            );
            response = professionalResponse;
        }
        if (response != null) {
            response.setSharedAttatchments(
                    sessionFileService.updateSessionFiles(sessionId, request.getSharedAttatchments(), SHARED)
            );
        }
        save(session);
        m.map(session, response);
        return response;
    }

    private void insertCompletedSessions(Therapy therapy) {
        Date today = new Date();
        Session nextSession = getById(therapy.getNextSessionId());
        while (nextSession.getDate().before(today)) {
            Date nextSessionDate = getNextOccurrence(therapy.getSchedule(), nextSession.getDate());
            nextSession = Session.builder()
                    .therapyId(therapy.getId())
                    .date(nextSessionDate)
                    .build();
            save(nextSession);
            therapy.setNextSessionId(nextSession.getId());
        }
        therapyService.save(therapy);
    }

    private void validateNextSessionUpdateRequest(TherapyScheduleUpdate update, Session nextSession) {
        if (update.getNextSessionDate().before(nextSession.getDate())) {
            throw new ApiRequestException(INVALID_NEXT_SESSION_DATE);
        }
        long differenceInMillis = Math.abs(nextSession.getDate().getTime() - new Date().getTime());
        if (differenceInMillis < MILLIS_IN_A_DAY) {
            throw new ApiRequestException(UPDATE_NEEDS_24_HOURS_DIFFERENCE);
        }
    }

    // The oldSchedule will be added back and the new one will be removed from his agenda
    private List<WeekDaySchedule> updateProfessionalSchedule(User professional, List<WeekDaySchedule> oldTherapySchedule, List<WeekDaySchedule> newTherapySchedule) {
        List<WeekDaySchedule> agenda = professional.getProfessionalInfo().getAgenda();
        List<WeekDaySchedule> availability = professional.getProfessionalInfo().getAvailability();

        if (fitsIn(agenda, oldTherapySchedule)) {
            mergeWith(availability, oldTherapySchedule);
        } else if (overlaps(agenda, oldTherapySchedule)) {
            mergeWith(availability, oldTherapySchedule, agenda);
        }

        if (fitsIn(availability, newTherapySchedule)) {
            removeFrom(availability, newTherapySchedule);
        } else if (fitsIn(agenda, newTherapySchedule)) {
            // undo the old schedule removal to throw an exception
            removeFrom(availability, oldTherapySchedule);
            throw new ApiRequestException(THERAPY_TIME_CONFLICT);
        } else if (overlaps(availability, newTherapySchedule)) {
            removeFrom(availability, newTherapySchedule);
        }
        return availability;
    }
}
