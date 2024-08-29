package com.mindzone.service.interfaces;

import com.mindzone.dto.request.MzPageRequest;
import com.mindzone.dto.request.ProfessionalSessionRequest;
import com.mindzone.dto.request.SessionRequest;
import com.mindzone.dto.request.TherapyScheduleUpdate;
import com.mindzone.dto.response.SessionResponse;
import com.mindzone.dto.response.TherapyResponse;
import com.mindzone.dto.response.listed.ListedSession;
import com.mindzone.model.user.User;
import org.springframework.data.domain.Page;

public interface SessionService {
    Page<ListedSession> getAll(User user, String therapyId, MzPageRequest mzPageRequest);

    TherapyResponse updateSchedule(User professional, String therapyId, TherapyScheduleUpdate update);

    SessionResponse get(User user, String sessionId);

    SessionResponse update(User user, String sessionId, SessionRequest sessionRequest);
}
