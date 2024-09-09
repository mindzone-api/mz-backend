package com.mindzone.repository;

import com.mindzone.model.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReportRepository extends MongoRepository<Report, String> {

    List<Report> findAllByTherapyId(String therapyId);
}
