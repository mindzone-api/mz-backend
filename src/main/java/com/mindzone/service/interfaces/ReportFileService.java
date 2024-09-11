package com.mindzone.service.interfaces;

import com.mindzone.model.ReportFile;
import com.mindzone.model.therapy.SessionFile;

import java.util.List;

public interface ReportFileService {
    void save(ReportFile model);

    ReportFile getById(String id);

    List<ReportFile> getAll(String reportId);

    List<ReportFile> updateReportFiles(String reportId, List<ReportFile> newReportFiles);

    void deleteAll(String reportId);
}
