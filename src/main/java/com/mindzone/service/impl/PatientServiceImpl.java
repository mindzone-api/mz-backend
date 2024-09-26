package com.mindzone.service.impl;

import com.mindzone.dto.request.UserRequest;
import com.mindzone.dto.response.UserResponse;
import com.mindzone.dto.response.listed.ListedProfessional;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.User;
import com.mindzone.repository.TherapyRepository;
import com.mindzone.service.interfaces.PatientService;
import com.mindzone.service.interfaces.UserService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

import static com.mindzone.enums.TherapyStatus.APPROVED;

@AllArgsConstructor
@Service
public class PatientServiceImpl implements PatientService {

    private TherapyRepository therapyRepository;
    private UltimateModelMapper m;
    private UserService userService;

    @Override
    @Cacheable("user")
    public List<ListedProfessional> getMyProfessionals(User user) {
        List<Therapy> therapies = therapyRepository.findAllByPatientIdAndTherapyStatus(user.getId(), APPROVED);
        List<ListedProfessional> professionals = new ArrayList<>();
        for (Therapy therapy : therapies) {
            User professional = userService.getById(therapy.getProfessionalId());
            ListedProfessional listedProfessional = m.map(professional, ListedProfessional.class);
            listedProfessional.setActive(therapy.getActive());
            professionals.add(listedProfessional);
        }
        return professionals;
    }

    @Override
    public UserResponse update(User patient, UserRequest request) {
        m.map(request, patient);
        userService.save(patient);
        return m.map(patient, UserResponse.class);
    }


}
