package com.mindzone.service.interfaces;

import com.mindzone.dto.request.TherapyRequest;
import com.mindzone.dto.request.TherapyResponse;
import com.mindzone.dto.response.ListedProfessional;
import com.mindzone.model.user.User;

import java.util.List;

public interface PatientService {
    List<ListedProfessional> getMyProfessionals(User user);

    TherapyResponse requestTherapy(TherapyRequest therapyRequest);
}
