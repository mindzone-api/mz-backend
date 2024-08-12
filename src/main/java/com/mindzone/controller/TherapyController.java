package com.mindzone.controller;

import com.mindzone.dto.request.TherapyRequest;
import com.mindzone.dto.response.TherapyResponse;
import com.mindzone.enums.Role;
import com.mindzone.model.user.User;
import com.mindzone.service.interfaces.TherapyService;
import com.mindzone.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mindzone.constants.Constants.V1;

@RestController
@AllArgsConstructor
@RequestMapping(V1 + "/therapy")
public class TherapyController {

    private UserService userService;
    private TherapyService therapyService;

    @PostMapping("/request")
    public ResponseEntity<TherapyResponse> requestTherapy(JwtAuthenticationToken token, @RequestBody TherapyRequest therapyRequest) {
        User user = userService.validateUser(token, Role.PATIENT);
        return ResponseEntity.ok(therapyService.requestTherapy(therapyRequest, user));
    }
}
