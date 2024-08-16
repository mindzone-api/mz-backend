package com.mindzone.controller;

import com.mindzone.dto.request.TherapyRequest;
import com.mindzone.dto.request.TherapyRequestAnalysis;
import com.mindzone.dto.response.TherapyResponse;
import com.mindzone.dto.response.listed.ListedTherapy;
import com.mindzone.model.user.User;
import com.mindzone.service.interfaces.TherapyService;
import com.mindzone.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.mindzone.constants.Constants.V1;
import static com.mindzone.enums.Role.PATIENT;
import static com.mindzone.enums.Role.PROFESSIONAL;

@RestController
@AllArgsConstructor
@RequestMapping(V1 + "/therapy")
public class TherapyController {

    private UserService userService;
    private TherapyService therapyService;

    @PostMapping("/request")
    public ResponseEntity<TherapyResponse> requestTherapy(JwtAuthenticationToken token, @RequestBody TherapyRequest therapyRequest) {
        User patient = userService.validateUser(token, PATIENT);
        return ResponseEntity.ok(therapyService.requestTherapy(therapyRequest, patient));
    }

    @PutMapping("request/{id}")
    public ResponseEntity<TherapyResponse> updateRequest(
            JwtAuthenticationToken token,
            @PathVariable String id,
            @RequestBody TherapyRequest therapyRequest
    ) {
        User patient = userService.validateUser(token, PATIENT);
        return ResponseEntity.ok(therapyService.updateRequest(patient, id, therapyRequest));
    }

    @DeleteMapping("request/{id}")
    public ResponseEntity<TherapyResponse> deleteRequest(JwtAuthenticationToken token, @PathVariable String id) {
        User patient = userService.validateUser(token, PATIENT);
        return ResponseEntity.ok(therapyService.deleteRequest(patient, id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TherapyResponse> get(JwtAuthenticationToken token, @PathVariable String id) {
        User user = userService.validateUser(token);
        return ResponseEntity.ok(therapyService.get(user, id));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ListedTherapy>> getAll(JwtAuthenticationToken token) {
        User user = userService.validateUser(token);
        return ResponseEntity.ok(therapyService.getAll(user));
    }

    @PutMapping("/analyse/{id}")
    public ResponseEntity<TherapyResponse> analyseRequest(
            JwtAuthenticationToken token,
            @PathVariable String id,
            @RequestBody TherapyRequestAnalysis analysis
            ) {
        User professional = userService.validateUser(token, PROFESSIONAL);
        return ResponseEntity.ok(therapyService.analyseRequest(professional, id, analysis));
    }

}
