package com.mindzone.service.impl;

import com.mindzone.dto.request.TherapyRequest;
import com.mindzone.dto.response.TherapyResponse;
import com.mindzone.dto.response.listed.ListedPatient;
import com.mindzone.dto.response.listed.ListedProfessional;
import com.mindzone.enums.TherapyStatus;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.ProfessionalInfo;
import com.mindzone.model.user.User;
import com.mindzone.repository.TherapyRepository;
import com.mindzone.service.interfaces.PatientService;
import com.mindzone.service.interfaces.UserService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

import static com.mindzone.exception.ExceptionMessages.PATIENT_IS_ALREADY_IN_THERAPY_WITH_THIS_PROFESSION;
import static com.mindzone.exception.ExceptionMessages.THERAPY_MODALITY_NOT_ACCEPTED;

@AllArgsConstructor
@Service
public class PatientServiceImpl implements PatientService {

    private TherapyRepository therapyRepository;
    private UltimateModelMapper m;
    private UserService userService;

    @Override
    public List<ListedProfessional> getMyProfessionals(User user) {
        List<Therapy> therapies = therapyRepository.findAllByPatientIdAndTherapyStatus(user.getId(), TherapyStatus.APPROVED);
        List<ListedProfessional> professionals = new ArrayList<>();
        for (Therapy therapy : therapies) {
            User professional = userService.getById(therapy.getProfessionalId());
            ListedProfessional listedProfessional = m.map(professional, ListedProfessional.class);
            listedProfessional.setActive(therapy.getActive());
            professionals.add(listedProfessional);
        }
        return professionals;
    }


}
