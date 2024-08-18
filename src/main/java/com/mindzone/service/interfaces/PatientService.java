package com.mindzone.service.interfaces;

import com.mindzone.dto.request.UserRequest;
import com.mindzone.dto.response.UserResponse;
import com.mindzone.dto.response.listed.ListedProfessional;
import com.mindzone.model.user.User;

import java.util.List;

public interface PatientService {
    List<ListedProfessional> getMyProfessionals(User user);

    UserResponse update(User patient, UserRequest request);
}
