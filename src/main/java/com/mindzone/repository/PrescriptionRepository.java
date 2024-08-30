package com.mindzone.repository;

import com.mindzone.model.therapy.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface PrescriptionRepository extends MongoRepository<Prescription, String> {

    Page<Prescription> findByTherapyId(String therapyId, Pageable pageable);

    @Query("{ 'therapyId': ?0, $or: [ { 'until': null }, { 'until': { $gte: ?1 } } ] }")
    List<Prescription> getActivePrescriptions(String therapyId, Date currentDate);
}
