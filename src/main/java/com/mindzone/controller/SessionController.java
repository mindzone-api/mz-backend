package com.mindzone.controller;

import com.mindzone.dto.request.MzPageRequest;
import com.mindzone.dto.request.TherapyScheduleUpdate;
import com.mindzone.dto.response.TherapyResponse;
import com.mindzone.dto.response.listed.ListedSession;
import com.mindzone.model.user.User;
import com.mindzone.service.interfaces.SessionService;
import com.mindzone.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static com.mindzone.constants.Constants.V1;
import static com.mindzone.enums.Role.PROFESSIONAL;

@RestController
@AllArgsConstructor
@RequestMapping(V1 + "/sessions/{therapyId}")
public class SessionController {

    private UserService userService;
    private SessionService sessionService;

    @GetMapping
    public ResponseEntity<Page<ListedSession>> getAll(
            JwtAuthenticationToken token,
            @PathVariable String therapyId,
            @RequestBody MzPageRequest mzPageRequest
            ) {
        User user = userService.validateUser(token);
        return ResponseEntity.ok(sessionService.getAll(user, therapyId, mzPageRequest));
    }

    @PutMapping
    public ResponseEntity<TherapyResponse> updateSchedule(
            JwtAuthenticationToken token,
            @PathVariable String therapyId,
            @RequestBody TherapyScheduleUpdate update
            ) {
        User professional = userService.validateUser(token, PROFESSIONAL);
        return ResponseEntity.ok(sessionService.updateSchedule(professional, therapyId, update));
    }

}
