package com.mindzone.service.impl;

import com.mindzone.dto.request.MzPageRequest;
import com.mindzone.dto.response.listed.ListedSession;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.therapy.Session;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.User;
import com.mindzone.repository.SessionRepository;
import com.mindzone.service.interfaces.SessionService;
import com.mindzone.service.interfaces.TherapyService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.Date;

import static com.mindzone.exception.ExceptionMessages.SESSION_NOT_FOUND;
import static com.mindzone.exception.ExceptionMessages.UPDATE_NEEDS_24_HOURS_DIFFERENCE;
import static com.mindzone.util.DateUtil.MILLIS_IN_A_DAY;
import static com.mindzone.util.WeekDayScheduleUtil.getNextOccurrence;

@AllArgsConstructor
@Service
public class SessionServiceImpl implements SessionService {

    private TherapyService therapyService;
    private SessionRepository sessionRepository;
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

    private void validateNextSession(Date nextSession) {
        long differenceInMillis = Math.abs(nextSession.getTime() - new Date().getTime());
        if (differenceInMillis >= MILLIS_IN_A_DAY) {
            throw new ApiRequestException(UPDATE_NEEDS_24_HOURS_DIFFERENCE);
        }
    }
}
