package com.mindzone.controller;

import com.mindzone.dto.request.QuestionnaireRequest;
import com.mindzone.dto.response.QuestionnaireResponse;
import com.mindzone.model.user.User;
import com.mindzone.service.interfaces.QuestionnaireService;
import com.mindzone.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static com.mindzone.constants.Constants.V1;
import static com.mindzone.enums.Role.PATIENT;

@RestController
@AllArgsConstructor
@RequestMapping(V1 + "/questionnaire")
public class QuestionnaireController {

    private UserService userService;
    private QuestionnaireService questionnaireService;

    @PostMapping
    public ResponseEntity<QuestionnaireResponse> create(JwtAuthenticationToken token, @RequestBody QuestionnaireRequest request) {
        User patient = userService.validate(token, PATIENT);
        return ResponseEntity.ok(questionnaireService.create(patient, request));
    }

    @GetMapping("/{questionnaireId}")
    public ResponseEntity<QuestionnaireResponse> get(JwtAuthenticationToken token, @PathVariable String questionnaireId) {
        User user = userService.validate(token);
        return ResponseEntity.ok(questionnaireService.get(user, questionnaireId));
    }
}
