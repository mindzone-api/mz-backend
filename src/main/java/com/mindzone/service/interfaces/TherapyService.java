package com.mindzone.service.interfaces;

import com.mindzone.dto.request.TherapyRequest;
import com.mindzone.dto.request.TherapyRequestAnalysis;
import com.mindzone.dto.response.TherapyResponse;
import com.mindzone.dto.response.listed.ListedTherapy;
import com.mindzone.enums.TherapyStatus;
import com.mindzone.model.user.User;

import java.util.List;

public interface TherapyService {

    TherapyResponse requestTherapy(TherapyRequest therapyRequest, User patient);
    TherapyResponse updateRequest(User patient, String id, TherapyRequest therapyRequest);
    TherapyResponse deleteRequest(User patient, String id);
    TherapyResponse get(User user, String id);
    List<ListedTherapy> getAll(User user);
    TherapyResponse analyseRequest(User professional, String id, TherapyRequestAnalysis analysis);

}
