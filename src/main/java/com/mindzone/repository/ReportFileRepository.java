package com.mindzone.repository;

import com.mindzone.model.ReportFile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReportFileRepository extends MongoRepository<ReportFile, String> {

    List<ReportFile> findAllByReportId(String reportId);
}
