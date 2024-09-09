package com.mindzone.repository;

import com.mindzone.model.therapy.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<Report, String> {
}
