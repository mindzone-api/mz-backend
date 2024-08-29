package com.mindzone.service.impl;

import com.mindzone.dto.request.PrescritionRequest;
import com.mindzone.dto.response.PrescriptionResponse;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.therapy.Prescription;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.User;
import com.mindzone.repository.PrescriptionRepository;
import com.mindzone.service.interfaces.MailService;
import com.mindzone.service.interfaces.PrescriptionService;
import com.mindzone.service.interfaces.TherapyService;
import com.mindzone.service.interfaces.UserService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.mindzone.constants.MailsBody.prescriptionCreationMail;
import static com.mindzone.exception.ExceptionMessages.PRESCRIPTION_NOT_FOUND;

@Service
@AllArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private TherapyService therapyService;
    private PrescriptionRepository prescriptionRepository;
    private UserService userService;
    private MailService mailService;
    private UltimateModelMapper m;

    @Override
    public void save(Prescription model) {
        model.updateDates();
        prescriptionRepository.save(model);
    }

    @Override
    public PrescriptionResponse create(User psychiatrist, PrescritionRequest request) {
        Therapy therapy = therapyService.getById(request.getTherapyId());
        therapyService.canManage(psychiatrist, therapy);
        therapyService.isActive(therapy);

        Prescription prescription = m.map(request, Prescription.class);
        save(prescription);

        mailService.sendMail(
                prescriptionCreationMail(
                        userService.getById(therapy.getPatientId()).getEmail(),
                        psychiatrist.getName()
                )
        );

        return m.map(prescription, PrescriptionResponse.class);
    }

    @Override
    public PrescriptionResponse get(User user, String prescriptionId) {
        Prescription prescription = getById(prescriptionId);
        Therapy therapy = therapyService.getById(prescription.getTherapyId());
        therapyService.canAccess(user, therapy);
        therapyService.isApproved(therapy);
        return m.map(prescription, PrescriptionResponse.class);
    }
    
    private Prescription getById(String prescriptionId) {
        return prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new ApiRequestException(PRESCRIPTION_NOT_FOUND));
    }
}
