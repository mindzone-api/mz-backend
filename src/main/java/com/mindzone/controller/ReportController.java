package com.mindzone.controller;


import com.mindzone.dto.request.ReportRequest;
import com.mindzone.dto.response.ReportResponse;
import com.mindzone.dto.response.listed.ListedReportResponse;
import com.mindzone.model.user.User;
import com.mindzone.service.interfaces.ReportService;
import com.mindzone.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mindzone.constants.Constants.V1;
import static com.mindzone.enums.Role.PROFESSIONAL;

@RestController
@AllArgsConstructor
@RequestMapping(V1 + "/report")
public class ReportController {

    private UserService userService;
    private ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponse> create(JwtAuthenticationToken token, @RequestBody ReportRequest request) {
        User professional = userService.validate(token, PROFESSIONAL);
        return ResponseEntity.ok(reportService.create(professional, request));
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ReportResponse> get(JwtAuthenticationToken token, @PathVariable String reportId) {
        User professional = userService.validate(token, PROFESSIONAL);
        return ResponseEntity.ok(reportService.get(professional, reportId));
    }

    @GetMapping("/therapy/{therapyId}")
    public ResponseEntity<List<ListedReportResponse>> getAll(JwtAuthenticationToken token, @PathVariable String therapyId) {
        User professional = userService.validate(token, PROFESSIONAL);
        return ResponseEntity.ok(reportService.getAll(professional, therapyId));
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<ReportResponse> update(
            JwtAuthenticationToken token,
            @RequestBody ReportRequest request,
            @PathVariable String reportId
    ) {
        User professional = userService.validate(token, PROFESSIONAL);
        return ResponseEntity.ok(reportService.update(professional, reportId, request));
    }
}
