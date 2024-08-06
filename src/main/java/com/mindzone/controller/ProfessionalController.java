package com.mindzone.controller;

import com.mindzone.dto.response.ListedPatient;
import com.mindzone.enums.Role;
import com.mindzone.model.user.User;
import com.mindzone.service.interfaces.ProfessionalService;
import com.mindzone.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.mindzone.constants.Constants.V1;

@RestController
@AllArgsConstructor
@RequestMapping(V1 + "/professional")
public class ProfessionalController {

    private UserService userService;
    private ProfessionalService professionalService;

    @GetMapping("my-patients")
    public ResponseEntity<List<ListedPatient>> getMyPatients(JwtAuthenticationToken token) {
        User user = userService.validateUser(token, Role.PROFESSIONAL);
        return ResponseEntity.ok(professionalService.getMyPatients(user));
    }

}
