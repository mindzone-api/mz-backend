package com.mindzone.service.impl;

import com.mindzone.dto.request.MzPageRequest;
import com.mindzone.dto.request.TherapyScheduleUpdate;
import com.mindzone.dto.response.TherapyResponse;
import com.mindzone.dto.response.listed.ListedSession;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.therapy.Session;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.User;
import com.mindzone.model.user.WeekDaySchedule;
import com.mindzone.repository.SessionRepository;
import com.mindzone.service.interfaces.MailService;
import com.mindzone.service.interfaces.SessionService;
import com.mindzone.service.interfaces.TherapyService;
import com.mindzone.service.interfaces.UserService;
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
import static com.mindzone.constants.MailsBody.therapyUpdateMail;
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
    private UltimateModelMapper m;

    private Session getById(String id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(SESSION_NOT_FOUND));
    }
    private void save(Session session) {
        session.setUpdatedAt(new Date());
        sessionRepository.save(session);
    }

    @Override
    public Page<ListedSession> getAll(User user, String therapyId, MzPageRequest mzPageRequest) {
        Therapy therapy = therapyService.getById(therapyId);
        therapyService.canAccess(user, therapy);
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
        therapyService.canManage(professional, therapy);
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
