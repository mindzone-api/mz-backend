package com.mindzone.service.interfaces;

import com.mindzone.dto.request.MzPageRequest;
import com.mindzone.dto.response.listed.ListedSession;
import com.mindzone.model.user.User;
import org.springframework.data.domain.Page;

public interface SessionService {
    Page<ListedSession> getAll(User user, String therapyId, MzPageRequest mzPageRequest);
}
