package com.mindzone.service.interfaces;

import com.mindzone.dto.request.ActivePrescriptionsRequest;
import com.mindzone.dto.request.MzPageRequest;
import com.mindzone.dto.request.PrescritionRequest;
import com.mindzone.dto.response.PrescriptionResponse;
import com.mindzone.dto.response.listed.ListedPrescription;
import com.mindzone.model.therapy.Prescription;
import com.mindzone.model.user.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PrescriptionService {
    void save(Prescription model);

    PrescriptionResponse create(User psychiatrist, PrescritionRequest request);

    PrescriptionResponse get(User user, String prescriptionId);

    PrescriptionResponse update(User psychiatrist, PrescritionRequest request, String prescriptionId);

    Page<ListedPrescription> getAll(User user, String therapyId, MzPageRequest pageRequest);

    PrescriptionResponse delete(User psychiatrist, String prescriptionId);

    List<ListedPrescription> getActivePrecriptions(User user, String therapyId, ActivePrescriptionsRequest date);
}
