package com.mindzone.repository;

import com.mindzone.model.therapy.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PrescriptionRepository extends MongoRepository<Prescription, String> {

    Page<Prescription> findByTherapyId(String therapyId, Pageable pageable);
}
