package com.mindzone.service.interfaces;

import com.mindzone.dto.response.ListedProfessional;
import com.mindzone.model.user.User;

import java.util.List;

public interface PatientService {
    List<ListedProfessional> getMyProfessionals(User user);
}
