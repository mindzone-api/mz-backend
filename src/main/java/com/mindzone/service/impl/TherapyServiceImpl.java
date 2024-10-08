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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mindzone.constants.MailsBody.therapyUpdateMail;
import static com.mindzone.enums.Role.PATIENT;
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
        model.updateDates();
        therapyRepository.save(model);
    }

    @Override
    public Therapy getById(String id) {
        return therapyRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(THERAPY_NOT_FOUND));
    }

    @Override
    public void canAccess(User user, Therapy therapy) {
        if (!therapy.getPatientId().equals(user.getId()) && !therapy.getProfessionalId().equals(user.getId())) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
        }
        isApproved(therapy);
    }

    @Override
    public void canManage(User user, Therapy therapy) {
        if (!therapy.getPatientId().equals(user.getId()) && !therapy.getProfessionalId().equals(user.getId())) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
        }
        isActive(therapy);
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
    @Cacheable("therapyGet")
    public TherapyResponse get(User user, String id) {
        Therapy therapy = getById(id);
        canAccess(user, therapy);
        return m.map(therapy, TherapyResponse.class);
    }

    @Override
    public boolean hasActiveTherapy(User professional, User patient) {
        return therapyRepository.findByProfessionalIdAndPatientIdAndActiveIsTrue(professional.getId(), patient.getId()).isPresent();
    }

    @Override
    @Cacheable("therapyGetAll")
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

    @Override
    public TherapyResponse cancel(User user, String therapyId) {
        Therapy therapy = getById(therapyId);
        canManage(user, therapy);
        therapy.setActive(false);
        save(therapy);
        return m.map(therapy, TherapyResponse.class);
    }
}
