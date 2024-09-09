package com.mindzone.service.impl;

import com.mindzone.dto.request.ReportRequest;
import com.mindzone.dto.response.ReportResponse;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.Report;
import com.mindzone.model.ReportFile;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.User;
import com.mindzone.repository.ReportRepository;
import com.mindzone.service.interfaces.ReportFileService;
import com.mindzone.service.interfaces.ReportService;
import com.mindzone.service.interfaces.TherapyService;
import com.mindzone.service.interfaces.UserService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.mindzone.exception.ExceptionMessages.REPORT_NOT_FOUND;
import static com.mindzone.exception.ExceptionMessages.USER_UNAUTHORIZED;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private ReportRepository reportRepository;
    private ReportFileService fileService;
    private UltimateModelMapper m;
    private UserService userService;
    private TherapyService therapyService;

    @Override
    public void save(Report model) {
        model.updateDates();
        reportRepository.save(model);
    }

    @Override
    public void canAccessReports(User user, Therapy therapy) {
        if (
                !therapy.getProfessionalId().equals(user.getId()) &&
                !userService.isAlly(user, therapy)
        ) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
        }
    }

    @Override
    public void canManageReports(User user, Therapy therapy) {
        if (!therapy.getProfessionalId().equals(user.getId())) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
        }
    }

    @Override
    public Report getById(String id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(REPORT_NOT_FOUND));
    }

    @Override
    public ReportResponse create(User professional, ReportRequest request) {
        Therapy therapy = therapyService.getById(request.getTherapyId());
        canManageReports(professional, therapy);
        therapyService.isActive(therapy);
        Report report = m.map(request, Report.class);
        save(report);
        ReportResponse response = m.map(report, ReportResponse.class);
        response.setAttachments(new ArrayList<>());
        for (ReportFile attachment : request.getAttachments()) {
            attachment.setReportId(report.getId());
            fileService.save(attachment);
            response.getAttachments().add(attachment);
        }
        return response;
    }

    @Override
    public ReportResponse get(User professional, String reportId) {
        Report report = getById(reportId);
        Therapy therapy = therapyService.getById(report.getTherapyId());
        canAccessReports(professional, therapy);
        therapyService.isApproved(therapy);
        ReportResponse response = m.map(report, ReportResponse.class);
        response.setAttachments(fileService.getAll(reportId));
        return response;
    }
}
