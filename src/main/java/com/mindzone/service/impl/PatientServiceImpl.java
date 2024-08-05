package com.mindzone.service.impl;

import com.mindzone.dto.request.TherapyRequest;
import com.mindzone.dto.response.TherapyResponse;
import com.mindzone.dto.response.ListedProfessional;
import com.mindzone.enums.TherapyStatus;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.User;
import com.mindzone.repository.TherapyRepository;
import com.mindzone.service.interfaces.PatientService;
import com.mindzone.service.interfaces.UserService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class PatientServiceImpl implements PatientService {

    private TherapyRepository therapyRepository;
    private UltimateModelMapper m;
    private UserService userService;

    @Override
    public List<ListedProfessional> getMyProfessionals(User user) {
        List<Therapy> therapies = therapyRepository.findAllByPatientIdAndTherapyStatus(user.getId(), TherapyStatus.APPROVED);
        List<User> professionals = new ArrayList<>();
        therapies.forEach(therapy ->
                professionals.add(userService.getById(therapy.getProfessionalId()))
        );
        return m.mapToList(professionals, ListedProfessional.class);
    }

    @Override // FIXME this feature still needs to be done properly
    public TherapyResponse requestTherapy(TherapyRequest therapyRequest, String userId) {
        Therapy therapy = m.map(therapyRequest, Therapy.class);
        therapy.setPatientId(userId);
        therapy.setTherapyStatus(TherapyStatus.PENDING);
        therapyRepository.save(therapy);
        return m.map(therapy, TherapyResponse.class);
    }
}
