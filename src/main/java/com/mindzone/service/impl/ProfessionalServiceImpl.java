package com.mindzone.service.impl;

import com.mindzone.dto.response.UserResponse;
import com.mindzone.dto.response.listed.ListedPatient;
import com.mindzone.enums.TherapyStatus;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.User;
import com.mindzone.model.user.WeekDaySchedule;
import com.mindzone.repository.TherapyRepository;
import com.mindzone.service.interfaces.ProfessionalService;
import com.mindzone.service.interfaces.UserService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.mindzone.dto.response.listed.ListedAlly;
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
        List<ListedPatient> patients = new ArrayList<>();
        for (Therapy therapy : therapies) {
            User patient = userService.getById(therapy.getPatientId());
            ListedPatient listedPatient = m.map(patient, ListedPatient.class);
            listedPatient.setActive(therapy.getActive());
            patients.add(listedPatient);
        }
        return patients;
    }

    @Override
    public List<ListedAlly> getMyAllies(User user) {
        List<Therapy> therapies = therapyRepository.findAllByProfessionalIdAndTherapyStatus(user.getId(), TherapyStatus.APPROVED);
        List<ListedAlly> allies = new ArrayList<>();
        for (Therapy therapy : therapies) {
            User patient = userService.getById(therapy.getPatientId());
            List<Therapy> patientTherapies = therapyRepository.findAllByPatientIdAndTherapyStatus(patient.getId(), TherapyStatus.APPROVED);
            for (Therapy patientTherapy : patientTherapies) {
                if (patientTherapy.getProfessionalId().equals(user.getId())) {
                    continue;
                }
                ListedAlly ally = m.map(userService.getById(patientTherapy.getProfessionalId()), ListedAlly.class);
                ally.setPatientId(patient.getId());
                ally.setActive(patientTherapy.getActive());
                allies.add(ally);
            }
        }
        return allies;
    }

    @Override
    public UserResponse updateAvailability(User user, List<WeekDaySchedule> schedule) {
        user.getProfessionalInfo().setAvailability(schedule);
        userService.save(user);
        return m.map(user, UserResponse.class);
    }
}
