package com.mindzone.controller;

import com.mindzone.dto.request.TherapyRequest;
import com.mindzone.dto.request.TherapyRequestAnalysis;
import com.mindzone.dto.response.TherapyResponse;
import com.mindzone.model.user.User;
import com.mindzone.service.interfaces.TherapyRequestService;
import com.mindzone.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static com.mindzone.constants.Constants.V1;
import static com.mindzone.enums.Role.PATIENT;
import static com.mindzone.enums.Role.PROFESSIONAL;

@RestController
@AllArgsConstructor
@RequestMapping(V1 + "/therapy/request")
public class TherapyRequestController {

    private UserService userService;
    private TherapyRequestService therapyRequestService;

    @PostMapping
    public ResponseEntity<TherapyResponse> createRequest(JwtAuthenticationToken token, @RequestBody TherapyRequest therapyRequest) {
        User patient = userService.validateUser(token, PATIENT);
        return ResponseEntity.ok(therapyRequestService.requestTherapy(therapyRequest, patient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TherapyResponse> updateRequest(
            JwtAuthenticationToken token,
            @PathVariable String id,
            @RequestBody TherapyRequest therapyRequest
    ) {
        User patient = userService.validateUser(token, PATIENT);
        return ResponseEntity.ok(therapyRequestService.updateRequest(patient, id, therapyRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TherapyResponse> deleteRequest(JwtAuthenticationToken token, @PathVariable String id) {
        User patient = userService.validateUser(token, PATIENT);
        return ResponseEntity.ok(therapyRequestService.deleteRequest(patient, id));
    }

    @PutMapping("/analyse/{id}")
    public ResponseEntity<TherapyResponse> analyseRequest(
            JwtAuthenticationToken token,
            @PathVariable String id,
            @RequestBody TherapyRequestAnalysis analysis
            ) {
        User professional = userService.validateUser(token, PROFESSIONAL);
        return ResponseEntity.ok(therapyRequestService.analyseRequest(professional, id, analysis));
    }

}
