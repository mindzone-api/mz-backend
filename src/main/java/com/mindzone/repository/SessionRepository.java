package com.mindzone.repository;

import com.mindzone.model.therapy.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionRepository extends MongoRepository<Session, String> {

    Page<Session> findByTherapyId(String therapyId, Pageable pageable);
}
