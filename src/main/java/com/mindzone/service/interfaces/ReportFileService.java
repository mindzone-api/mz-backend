package com.mindzone.service.interfaces;

import com.mindzone.model.ReportFile;

import java.util.List;

public interface ReportFileService {
    void save(ReportFile model);

    List<ReportFile> getAll(String reportId);
}
