package com.mindzone.service.impl;

import com.mindzone.dto.request.TherapyUpdate;
import com.mindzone.dto.response.TherapyResponse;
import com.mindzone.dto.response.listed.ListedTherapy;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.User;
import com.mindzone.repository.TherapyRepository;
import com.mindzone.service.interfaces.MailService;
import com.mindzone.service.interfaces.TherapyService;
import com.mindzone.service.interfaces.UserService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

import static com.mindzone.constants.MailsBody.therapyUpdateMail;
import static com.mindzone.enums.Role.PATIENT;
import static com.mindzone.enums.Role.PROFESSIONAL;
import static com.mindzone.enums.TherapyStatus.APPROVED;
import static com.mindzone.exception.ExceptionMessages.*;

@Service
@AllArgsConstructor
public class TherapyServiceImpl implements TherapyService {

    private TherapyRepository therapyRepository;
    private UserService userService;
    private UltimateModelMapper m;
    private MailService mailService;

    @Override
    public void save(Therapy model) {
        Date now = new Date();
        if (model.getCreatedAt() == null) {
            model.setCreatedAt(now);
        }
        model.setUpdatedAt(now);
        therapyRepository.save(model);
    }

    @Override
    public Therapy getById(String id) {
        return therapyRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(THERAPY_NOT_FOUND));
    }

    @Override
    public void canAccess(User user, Therapy therapy) {
        if (user.getRole() == PATIENT) {
            if (!therapy.getPatientId().equals(user.getId())) {
                throw new ApiRequestException(USER_UNAUTHORIZED);
            }
        } else if (user.getRole() == PROFESSIONAL) {
            List<Therapy> patientTherapies = therapyRepository.findAllByPatientIdAndTherapyStatus(therapy.getPatientId(), APPROVED);
            List<Therapy> patientTherapiesWithLoggedProfessional = patientTherapies.stream().filter(t -> t.getProfessionalId().equals(user.getId())).toList();
            if (patientTherapiesWithLoggedProfessional.isEmpty()) {
                throw new ApiRequestException(USER_UNAUTHORIZED);
            }
        }
    }

    @Override
    public void canManage(User user, Therapy therapy) {
        if (user.getRole() == PATIENT && !therapy.getPatientId().equals(user.getId())) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
        } else if (user.getRole() == PROFESSIONAL && !therapy.getProfessionalId().equals(user.getId())) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
        }
    }

    @Override
    public void isApproved(Therapy therapy) {
        if (therapy.getTherapyStatus() != APPROVED) {
            throw new ApiRequestException(THERAPY_IS_NOT_APPROVED);
        }
    }

    @Override
    public void isActive(Therapy therapy) {
        if (!therapy.getActive()) {
            throw new ApiRequestException(THERAPY_IS_NOT_ACTIVE);
        }
    }

    @Override
    public TherapyResponse get(User user, String id) {
        Therapy therapy = getById(id);
        canAccess(user, therapy);
        return m.map(therapy, TherapyResponse.class);
    }

    @Override
    public List<ListedTherapy> getAll(User user) {
        return (
                user.getRole() == PATIENT ?
                        m.listMap(therapyRepository.findAllByPatientId(user.getId()), ListedTherapy.class) :
                        m.listMap(therapyRepository.findAllByProfessionalId(user.getId()), ListedTherapy.class)
        );
    }

    @Override
    public TherapyResponse update(User professional, String id, TherapyUpdate therapyUpdate) {
        Therapy therapy = getById(id);
        canManage(professional, therapy);
        m.map(therapyUpdate, therapy);
        userService.save(professional);
        save(therapy);

        // notify patient about professional final analysis
        mailService.sendMail(
                therapyUpdateMail(userService.getById(therapy.getPatientId()).getEmail(), professional.getName())
        );
        return m.map(therapy, TherapyResponse.class);
    }
}
