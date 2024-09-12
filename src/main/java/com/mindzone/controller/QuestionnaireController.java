package com.mindzone.controller;

import com.mindzone.dto.request.MzPageRequest;
import com.mindzone.dto.request.QuestionnaireRequest;
import com.mindzone.dto.response.QuestionnaireResponse;
import com.mindzone.dto.response.listed.ListedQuestionnaire;
import com.mindzone.model.user.User;
import com.mindzone.service.interfaces.QuestionnaireService;
import com.mindzone.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("all/{userId}")
    public ResponseEntity<Page<ListedQuestionnaire>> getAll(
            JwtAuthenticationToken token,
            @PathVariable String userId,
            @RequestBody MzPageRequest pageRequest
            ) {
        User user = userService.validate(token);
        return ResponseEntity.ok(questionnaireService.getAll(user, userId, pageRequest));
    }

    @PutMapping("/{questionnaireId}")
    public ResponseEntity<QuestionnaireResponse> update(
            JwtAuthenticationToken token,
            @PathVariable String questionnaireId,
            @RequestBody QuestionnaireRequest request
    ) {
        User patient = userService.validate(token, PATIENT);
        return ResponseEntity.ok(questionnaireService.update(patient, questionnaireId, request));
    }

    @DeleteMapping("/{questionnaireId}")
    public ResponseEntity<QuestionnaireResponse> delete(JwtAuthenticationToken token, @PathVariable String questionnaireId) {
        User patient = userService.validate(token);
        return ResponseEntity.ok(questionnaireService.delete(patient, questionnaireId));
    }
}
