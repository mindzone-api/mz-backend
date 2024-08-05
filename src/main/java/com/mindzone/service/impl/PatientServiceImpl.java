package com.mindzone.service.impl;

import com.mindzone.dto.response.ListedProfessional;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.User;
import com.mindzone.repository.TherapyRepository;
import com.mindzone.repository.UserRepository;
import com.mindzone.service.interfaces.PatientService;
import com.mindzone.service.interfaces.UserService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.mindzone.exception.ExceptionMessages.PROFESSIONAL_NOT_FOUND;

@AllArgsConstructor
@Service
public class PatientServiceImpl implements PatientService {

    private TherapyRepository therapyRepository;
    private UltimateModelMapper m;
    private UserService userService;

    @Override
    public List<ListedProfessional> getMyProfessionals(User user) {
        List<Therapy> therapies = therapyRepository.findAllByPatientId(user.getId());
        List<User> professionals = new ArrayList<>();
        therapies.forEach(therapy ->
                professionals.add(userService.getById(therapy.getProfessionalId()))
        );
        return m.mapToList(professionals, ListedProfessional.class);
    }
}
