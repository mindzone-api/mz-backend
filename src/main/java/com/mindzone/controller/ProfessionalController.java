package com.mindzone.controller;

import com.mindzone.dto.request.SearchFilter;
import com.mindzone.dto.request.UserRequest;
import com.mindzone.dto.response.UserResponse;
import com.mindzone.dto.response.listed.ListedPatient;
import com.mindzone.dto.response.listed.ListedAlly;
import com.mindzone.dto.response.listed.ListedProfessional;
import com.mindzone.dto.response.listed.ListedTherapy;
import com.mindzone.model.user.User;
import com.mindzone.service.interfaces.ProfessionalService;
import com.mindzone.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mindzone.constants.Constants.V1;
import static com.mindzone.enums.Role.PROFESSIONAL;

@RestController
@AllArgsConstructor
@RequestMapping(V1 + "/professional")
public class ProfessionalController {

    private UserService userService;
    private ProfessionalService professionalService;

    @GetMapping("/my-patients")
    public ResponseEntity<List<ListedPatient>> getMyPatients(JwtAuthenticationToken token) {
        User user = userService.validate(token, PROFESSIONAL);
        return ResponseEntity.ok(professionalService.getMyPatients(user));
    }

    @GetMapping("/my-allies")
    public ResponseEntity<List<ListedAlly>> getMyAllies(JwtAuthenticationToken token) {
        User user = userService.validate(token, PROFESSIONAL);
        return ResponseEntity.ok(professionalService.getMyAllies(user));
    }

    @GetMapping("/my-allies-therapies")
    public ResponseEntity<List<ListedTherapy>> getMyAlliesTherapies(JwtAuthenticationToken token) {
        User user = userService.validate(token, PROFESSIONAL);
        return ResponseEntity.ok(professionalService.getMyAlliesTherapies(user));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ListedProfessional>> search(JwtAuthenticationToken token, @RequestBody SearchFilter filter) {
        userService.validate(token);
        return ResponseEntity.ok(userService.search(filter));
    }

    @PutMapping
    public ResponseEntity<UserResponse> update(JwtAuthenticationToken token, @RequestBody UserRequest request) {
        User professional = userService.validate(token, PROFESSIONAL);
        return ResponseEntity.ok(professionalService.update(professional, request));
    }

}
