package com.mindzone.repository;

import com.mindzone.model.Questionnaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.Optional;

public interface QuestionnaireRepository extends MongoRepository<Questionnaire, String> {

    @Query("{ $expr: { $and: [ " +
            "{ $eq: [ { $year: '$createdAt' }, { $year: ?0 } ] }, " +
            "{ $eq: [ { $month: '$createdAt' }, { $month: ?0 } ] }, " +
            "{ $eq: [ { $dayOfMonth: '$createdAt' }, { $dayOfMonth: ?0 } ] } " +
            "] } }")
    Optional<Questionnaire> findByDate(Date date);

    Page<Questionnaire> findByPatientId(String patientId, Pageable pageable);
}
