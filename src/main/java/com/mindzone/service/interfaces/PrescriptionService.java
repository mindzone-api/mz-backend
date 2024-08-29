package com.mindzone.service.interfaces;

import com.mindzone.dto.request.PrescritionRequest;
import com.mindzone.dto.response.PrescriptionResponse;
import com.mindzone.model.therapy.Prescription;
import com.mindzone.model.user.User;

public interface PrescriptionService {
    void save(Prescription model);

    PrescriptionResponse create(User psychiatrist, PrescritionRequest request);
}
