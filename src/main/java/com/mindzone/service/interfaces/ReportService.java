package com.mindzone.service.interfaces;

import com.mindzone.dto.request.ReportRequest;
import com.mindzone.dto.response.ReportResponse;
import com.mindzone.model.Report;
import com.mindzone.model.user.User;

public interface ReportService {

    void save(Report model);

    Report getById(String id);

    ReportResponse create(User professional, ReportRequest request);
}
