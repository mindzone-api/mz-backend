package com.mindzone.service.impl;

import com.mindzone.dto.request.ActivePrescriptionsRequest;
import com.mindzone.dto.request.MzPageRequest;
import com.mindzone.dto.request.PrescritionRequest;
import com.mindzone.dto.response.PrescriptionResponse;
import com.mindzone.dto.response.listed.ListedPrescription;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.Prescription;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.User;
import com.mindzone.repository.PrescriptionRepository;
import com.mindzone.service.interfaces.MailService;
import com.mindzone.service.interfaces.PrescriptionService;
import com.mindzone.service.interfaces.TherapyService;
import com.mindzone.service.interfaces.UserService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.mindzone.constants.MailsBody.*;
import static com.mindzone.exception.ExceptionMessages.*;
import static com.mindzone.exception.ExceptionMessages.USER_UNAUTHORIZED;

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
    public void canAccessPrescriptions(User user, Therapy therapy) {
        if (
                !therapy.getPatientId().equals(user.getId()) &&
                !therapy.getProfessionalId().equals(user.getId()) &&
                !userService.isAlly(user, therapy)
        ) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
        }
        therapyService.isApproved(therapy);
    }

    @Override
    public void canManagePrescriptions(User user, Therapy therapy) {
        if (!therapy.getProfessionalId().equals(user.getId())) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
        }
        therapyService.isActive(therapy);
    }

    @Override
    public PrescriptionResponse create(User psychiatrist, PrescritionRequest request) {
        Therapy therapy = therapyService.getById(request.getTherapyId());
        canManagePrescriptions(psychiatrist, therapy);

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
    @Cacheable("prescriptionGet")
    public PrescriptionResponse get(User user, String prescriptionId) {
        Prescription prescription = getById(prescriptionId);
        Therapy therapy = therapyService.getById(prescription.getTherapyId());
        canAccessPrescriptions(user, therapy);
        return m.map(prescription, PrescriptionResponse.class);
    }

    @Override
    public PrescriptionResponse update(User psychiatrist, PrescritionRequest request, String prescriptionId) {
        Therapy therapy = therapyService.getById(request.getTherapyId());
        canManagePrescriptions(psychiatrist, therapy);

        Prescription prescription = getById(prescriptionId);
        if (prescription.getUntil() != null && prescription.getUntil().before(new Date())) {
            throw new ApiRequestException(PRESCRIPTION_NOT_EDITABLE);
        }

        m.map(request, prescription);
        save(prescription);

        mailService.sendMail(
                prescriptionUpdateMail(
                        userService.getById(therapy.getPatientId()).getEmail(),
                        psychiatrist.getName()
                )
        );

        return m.map(prescription, PrescriptionResponse.class);
    }

    @Override
    @Cacheable("prescriptionGetAll")
    public Page<ListedPrescription> getAll(User user, String therapyId, MzPageRequest pageRequest) {
        Therapy therapy = therapyService.getById(therapyId);
        canAccessPrescriptions(user, therapy);


        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), sort);
        Page<Prescription> prescriptions = prescriptionRepository.findByTherapyId(therapyId, pageable);

        return m.pageMap(prescriptions, ListedPrescription.class);
    }

    @Override
    public PrescriptionResponse delete(User psychiatrist, String prescriptionId) {
        Prescription prescription = getById(prescriptionId);
        Therapy therapy = therapyService.getById(prescription.getTherapyId());
        canManagePrescriptions(psychiatrist, therapy);


        if (prescription.getUntil() != null && prescription.getUntil().before(new Date())) {
            throw new ApiRequestException(PRESCRIPTION_NOT_EDITABLE);
        }

        prescriptionRepository.delete(prescription);

        mailService.sendMail(
                prescriptionDeleteMail(
                        userService.getById(therapy.getPatientId()).getEmail(),
                        psychiatrist.getName()
                )
        );
        return m.map(prescription, PrescriptionResponse.class);
    }

    @Override
    @Cacheable("getActivePrecriptions")
    public List<ListedPrescription> getActivePrecriptions(
            User user,
            String therapyId,
            ActivePrescriptionsRequest date
    ) {
        Therapy therapy = therapyService.getById(therapyId);
        canAccessPrescriptions(user, therapy);

        List<Prescription> prescriptions = prescriptionRepository.getActivePrescriptions(therapyId, date.getDate());
        return m.listMap(prescriptions, ListedPrescription.class);
    }

    private Prescription getById(String prescriptionId) {
        return prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new ApiRequestException(PRESCRIPTION_NOT_FOUND));
    }
}
