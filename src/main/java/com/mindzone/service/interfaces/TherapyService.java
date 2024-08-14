package com.mindzone.service.interfaces;

import com.mindzone.dto.request.TherapyRequest;
import com.mindzone.dto.response.TherapyResponse;
import com.mindzone.model.user.User;

public interface TherapyService {

    TherapyResponse requestTherapy(TherapyRequest therapyRequest, User user);

    TherapyResponse get(User user, String id);
}
