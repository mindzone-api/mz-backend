package com.mindzone.controller;

import com.mindzone.dto.request.UserRequest;
import com.mindzone.dto.response.UserResponse;
import com.mindzone.dto.response.listed.ListedProfessional;
import com.mindzone.model.user.User;
import com.mindzone.service.interfaces.PatientService;
import com.mindzone.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mindzone.constants.Constants.V1;
import static com.mindzone.enums.Role.PATIENT;

@RestController
@AllArgsConstructor
@RequestMapping(V1 + "/patient")
public class PatientController {

    private UserService userService;
    private PatientService patientService;

    @GetMapping("/my-professionals")
    public ResponseEntity<List<ListedProfessional>> getMyProfessionals(JwtAuthenticationToken token) {
        User user = userService.validateUser(token, PATIENT);
        return ResponseEntity.ok(patientService.getMyProfessionals(user));
    }

    @PutMapping
    public ResponseEntity<UserResponse> update(JwtAuthenticationToken token, @RequestBody UserRequest request) {
        User patient = userService.validateUser(token, PATIENT);
        return ResponseEntity.ok(patientService.update(patient, request));
    }
}
