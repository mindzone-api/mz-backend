package com.mindzone.repository;

import com.mindzone.enums.TherapyStatus;
import com.mindzone.model.therapy.Therapy;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TherapyRepository extends MongoRepository<Therapy, String> {

    List<Therapy> findAllByPatientIdAndTherapyStatus(String patientId, TherapyStatus status);
    List<Therapy> findAllByProfessionalIdAndTherapyStatus(String professionalId, TherapyStatus status);

    List<Therapy> findAllByPatientId(String id);
    List<Therapy> findAllByProfessionalId(String id);
}
