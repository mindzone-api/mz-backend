package com.mindzone.service.impl;

import com.mindzone.dto.request.UserRequest;
import com.mindzone.dto.response.UserResponse;
import com.mindzone.dto.response.listed.ListedPatient;
import com.mindzone.dto.response.listed.ListedTherapy;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.User;
import com.mindzone.model.user.WeekDaySchedule;
import com.mindzone.repository.TherapyRepository;
import com.mindzone.service.interfaces.MailService;
import com.mindzone.service.interfaces.ProfessionalService;
import com.mindzone.service.interfaces.UserService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.mindzone.dto.response.listed.ListedAlly;
import java.util.ArrayList;
import java.util.List;

import static com.mindzone.constants.MailsBody.canceledTherapyRequestMail;
import static com.mindzone.enums.TherapyStatus.*;
import static com.mindzone.util.WeekDayScheduleUtil.fitsIn;
import static com.mindzone.util.WeekDayScheduleUtil.removeFrom;

@Service
@AllArgsConstructor
public class ProfessionalServiceImpl implements ProfessionalService {

    private TherapyRepository therapyRepository;
    private UltimateModelMapper m;
    private MailService mailService;
    private UserService userService;
    @Override
    @Cacheable("getMyPatients")
    public List<ListedPatient> getMyPatients(User user) {
        List<Therapy> therapies = therapyRepository.findAllByProfessionalIdAndTherapyStatus(user.getId(), APPROVED);
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
    @Cacheable("getMyAllies")
    public List<ListedAlly> getMyAllies(User user) {
        List<Therapy> therapies = therapyRepository.findAllByProfessionalIdAndTherapyStatus(user.getId(), APPROVED);
        List<ListedAlly> allies = new ArrayList<>();
        for (Therapy therapy : therapies) {
            User patient = userService.getById(therapy.getPatientId());
            List<Therapy> patientTherapies = therapyRepository.findAllByPatientIdAndTherapyStatus(patient.getId(), APPROVED);
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
    public UserResponse update(User professional, UserRequest request) {
        m.map(request, professional);
        updateProfessionalAvailability(professional);
        userService.save(professional);
        return m.map(professional, UserResponse.class);
    }

    @Override
    @Cacheable("getMyAlliesTherapies")
    public List<ListedTherapy> getMyAlliesTherapies(User professional) {
        List<ListedTherapy> response = new ArrayList<>();

        for (Therapy therapy : therapyRepository.findAllByProfessionalIdAndActiveIsTrue(professional.getId())) {
            List<Therapy> patientTherapies = therapyRepository.findAllByPatientIdAndActiveIsTrue(therapy.getPatientId());
            patientTherapies.removeIf(t -> t.getProfessionalId().equals(professional.getId()));
            response.addAll(m.listMap(patientTherapies, ListedTherapy.class));
        }
        return response;
    }

    private void updateProfessionalAvailability(User professional) {
        professional.getProfessionalInfo().setAvailability(
                userService.getAgendaCopy(
                        professional.getProfessionalInfo().getAgenda()
                )
        );
        List<WeekDaySchedule> availability = professional.getProfessionalInfo().getAvailability();

        // removing active therapies schedule from the professional's new availability
        List<Therapy> activeTherapies = therapyRepository.findAllByProfessionalIdAndActiveIsTrue(professional.getId());
        for (Therapy activeTherapy : activeTherapies) {
            if (fitsIn(availability, activeTherapy.getSchedule())) {
                removeFrom(availability, activeTherapy.getSchedule());
            }
        }

        // Canceling all pending requests that does not fit in the professional's new agenda
        List<Therapy> pendingTherapies = therapyRepository.findAllByProfessionalIdAndTherapyStatus(professional.getId(), PENDING);
        for (Therapy pendingTherapy : pendingTherapies) {
            if (!fitsIn(availability, pendingTherapy.getSchedule())) {
                User patientNotified = userService.getById(pendingTherapy.getPatientId());
                pendingTherapy.setTherapyStatus(CANCELED);
                therapyRepository.save(pendingTherapy);
                // notify patient about this occurence
                mailService.sendMail(canceledTherapyRequestMail(patientNotified.getEmail(), professional.getName()));
            }
        }

        professional.getProfessionalInfo().setAvailability(availability);
    }
}
