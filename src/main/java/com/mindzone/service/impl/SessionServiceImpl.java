package com.mindzone.service.impl;

import com.mindzone.dto.request.PageRequest;
import com.mindzone.dto.response.listed.ListedSession;
import com.mindzone.model.user.User;
import com.mindzone.service.interfaces.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SessionServiceImpl implements SessionService {
    @Override
    public Page<ListedSession> getAll(User user, String therapyId, PageRequest pageRequest) {
        return null;
    }
}
