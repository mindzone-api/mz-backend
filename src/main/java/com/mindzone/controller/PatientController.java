package com.mindzone.controller;

import com.mindzone.dto.request.SearchFilter;
import com.mindzone.dto.response.ListedProfessional;
import com.mindzone.enums.Role;
import com.mindzone.model.user.User;
import com.mindzone.service.interfaces.PatientService;
import com.mindzone.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.mindzone.constants.Constants.V1;

@RestController
@AllArgsConstructor
@RequestMapping(V1 + "/patient")
public class PatientController {

    private UserService userService;
    private PatientService patientService;

    @GetMapping("/search")
    public ResponseEntity<Page<ListedProfessional>> search(JwtAuthenticationToken token, @RequestBody SearchFilter filter) {
        userService.validateUser(token);
        return ResponseEntity.ok(userService.search(filter));
    }

    @GetMapping("/my-professionals")
    public ResponseEntity<List<ListedProfessional>> getMyProfessionals(JwtAuthenticationToken token) {
        User user = userService.validateUser(token, Role.PATIENT);
        return ResponseEntity.ok(patientService.getMyProfessionals(user));
    }
}
