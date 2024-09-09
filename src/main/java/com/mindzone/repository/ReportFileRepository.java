package com.mindzone.repository;

import com.mindzone.model.ReportFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportFileRepository extends MongoRepository<ReportFile, String> {
}
