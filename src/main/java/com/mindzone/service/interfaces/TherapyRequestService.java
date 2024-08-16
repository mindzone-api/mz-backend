package com.mindzone.service.interfaces;

import com.mindzone.dto.request.TherapyRequest;
import com.mindzone.dto.request.TherapyRequestAnalysis;
import com.mindzone.dto.response.TherapyResponse;
import com.mindzone.dto.response.listed.ListedTherapy;
import com.mindzone.model.user.User;

import java.util.List;

public interface TherapyRequestService {

    TherapyResponse requestTherapy(TherapyRequest therapyRequest, User patient);
    TherapyResponse updateRequest(User patient, String id, TherapyRequest therapyRequest);
    TherapyResponse deleteRequest(User patient, String id);
    TherapyResponse analyseRequest(User professional, String id, TherapyRequestAnalysis analysis);

}
