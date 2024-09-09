package com.mindzone.service.impl;

import com.mindzone.dto.request.ReportRequest;
import com.mindzone.dto.response.ReportResponse;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.Report;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.User;
import com.mindzone.repository.ReportRepository;
import com.mindzone.service.interfaces.ReportService;
import com.mindzone.service.interfaces.TherapyService;
import com.mindzone.service.interfaces.UserService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.mindzone.exception.ExceptionMessages.REPORT_NOT_FOUND;
import static com.mindzone.exception.ExceptionMessages.USER_UNAUTHORIZED;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private ReportRepository reportRepository;
    private UltimateModelMapper m;
    private UserService userService;
    private TherapyService therapyService;

    @Override
    public void save(Report model) {
        model.updateDates();
        reportRepository.save(model);
    }

    @Override
    public Report getById(String id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(REPORT_NOT_FOUND));
    }

    private void canAccess(User professional, Therapy therapy) {
        if (
                !professional.getId().equals(therapy.getProfessionalId()) &&
                !userService.isAlly(professional, therapy)
        ) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
        }
    }

    private void canManage(User professional, Therapy therapy) {
        if (!professional.getId().equals(therapy.getProfessionalId())) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
        }
    }

    @Override
    public ReportResponse create(User professional, ReportRequest request) {
        Therapy therapy = therapyService.getById(request.getTherapyId());
        therapyService.canManage(professional, therapy);
        therapyService.isActive(therapy);
        Report report = m.map(request, Report.class);
        save(report);
        return m.map(report, ReportResponse.class);
    }
}
