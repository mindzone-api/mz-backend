package com.mindzone.service.impl;

import com.mindzone.dto.request.MzPageRequest;
import com.mindzone.dto.response.listed.ListedSession;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.therapy.Session;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.User;
import com.mindzone.service.interfaces.SessionService;
import com.mindzone.service.interfaces.TherapyService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.mindzone.exception.ExceptionMessages.UPDATE_NEEDS_24_HOURS_DIFFERENCE;
import static com.mindzone.util.DateUtil.MILLIS_IN_A_DAY;
import static com.mindzone.util.WeekDayScheduleUtil.getNextOccurrence;

@AllArgsConstructor
@Service
public class SessionServiceImpl implements SessionService {

    private TherapyService therapyService;
    private UltimateModelMapper m;

    @Override
    public Page<ListedSession> getAll(User user, String therapyId, MzPageRequest mzPageRequest) {
        Therapy therapy = therapyService.getById(therapyId);
        therapyService.canAccess(user, therapy);
        therapyService.isApproved(therapy);

        insertCompletedSessions(therapy);

        List<ListedSession> sessions = m.mapToList(therapy.getSessions(), ListedSession.class);

        // reversing list order so it can be displayed from latest to oldest
        Collections.reverse(sessions);
        Pageable pageable = PageRequest.of(mzPageRequest.getPage(), mzPageRequest.getSize());
        return new PageImpl<>(sessions, pageable, sessions.size());
    }

    private void insertCompletedSessions(Therapy therapy) {
        if (therapy.getSessions() == null) {
            therapy.setSessions(new ArrayList<>());
        }
        List<Session> sessions = therapy.getSessions();

        Date today = new Date();
        while (therapy.getNextSession().getDate().before(today)) {
            sessions.add(therapy.getNextSession());
            Date nextSessionDate = getNextOccurrence(therapy.getSchedule(), therapy.getNextSession().getDate());
            therapy.setNextSession(new Session(nextSessionDate));
        }
        therapyService.save(therapy);
    }

    private void validateNextSession(Date nextSession) {
        long differenceInMillis = Math.abs(nextSession.getTime() - new Date().getTime());
        if (differenceInMillis >= MILLIS_IN_A_DAY) {
            throw new ApiRequestException(UPDATE_NEEDS_24_HOURS_DIFFERENCE);
        }
    }
}
