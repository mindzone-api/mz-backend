package com.mindzone.service.impl;

import com.mindzone.dto.response.TherapyResponse;
import com.mindzone.dto.response.listed.ListedTherapy;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.User;
import com.mindzone.repository.TherapyRepository;
import com.mindzone.service.interfaces.TherapyService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.mindzone.enums.Role.PATIENT;
import static com.mindzone.enums.Role.PROFESSIONAL;
import static com.mindzone.enums.TherapyStatus.APPROVED;
import static com.mindzone.exception.ExceptionMessages.THERAPY_NOT_FOUND;
import static com.mindzone.exception.ExceptionMessages.USER_UNAUTHORIZED;

@Service
@AllArgsConstructor
public class TherapyServiceImpl implements TherapyService {

    private TherapyRepository therapyRepository;
    private UltimateModelMapper m;

    public void save(Therapy therapy) {
        therapy.setUpdatedAt(new Date());
        therapyRepository.save(therapy);
    }

    public Therapy getById(String id) {
        return therapyRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(THERAPY_NOT_FOUND));
    }

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

    public void canManage(User user, Therapy therapy) {
        if (user.getRole() == PATIENT && !therapy.getPatientId().equals(user.getId())) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
        } else if (user.getRole() == PROFESSIONAL && !therapy.getProfessionalId().equals(user.getId())) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
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
                        m.mapToList(therapyRepository.findAllByPatientId(user.getId()), ListedTherapy.class) :
                        m.mapToList(therapyRepository.findAllByProfessionalId(user.getId()), ListedTherapy.class)
        );
    }
}
