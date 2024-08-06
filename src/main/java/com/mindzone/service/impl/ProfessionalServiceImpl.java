package com.mindzone.service.impl;

import com.mindzone.dto.response.ListedPatient;
import com.mindzone.dto.response.ListedProfessional;
import com.mindzone.enums.TherapyStatus;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.User;
import com.mindzone.repository.TherapyRepository;
import com.mindzone.service.interfaces.ProfessionalService;
import com.mindzone.service.interfaces.UserService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProfessionalServiceImpl implements ProfessionalService {

    private TherapyRepository therapyRepository;
    private UltimateModelMapper m;
    private UserService userService;
    @Override
    public List<ListedPatient> getMyPatients(User user) {
        List<Therapy> therapies = therapyRepository.findAllByProfessionalIdAndTherapyStatus(user.getId(), TherapyStatus.APPROVED);
        List<User> patients = new ArrayList<>();
        therapies.forEach(therapy ->
                patients.add(userService.getById(therapy.getPatientId()))
        );
        return m.mapToList(patients, ListedPatient.class);
    }
}
