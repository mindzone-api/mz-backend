package com.mindzone.controller;

import com.mindzone.dto.request.TherapyUpdate;
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
import static com.mindzone.enums.Role.PROFESSIONAL;

@RestController
@AllArgsConstructor
@RequestMapping(V1 + "/therapy")
public class TherapyController {

    private UserService userService;
    private TherapyService therapyService;

    @GetMapping("/{therapyId}")
    public ResponseEntity<TherapyResponse> get(JwtAuthenticationToken token, @PathVariable String therapyId) {
        User user = userService.validate(token);
        return ResponseEntity.ok(therapyService.get(user, therapyId));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ListedTherapy>> getAll(JwtAuthenticationToken token) {
        User user = userService.validate(token);
        return ResponseEntity.ok(therapyService.getAll(user));
    }

    @PutMapping("/{therapyId}")
    public ResponseEntity<TherapyResponse> update(
            JwtAuthenticationToken token,
            @PathVariable String therapyId,
            @RequestBody TherapyUpdate therapyUpdate
    ) {
        User professional = userService.validate(token, PROFESSIONAL);
        return ResponseEntity.ok(therapyService.update(professional, therapyId, therapyUpdate));
    }

    // TODO: delete a therapy (make inactive)

    @DeleteMapping("/{therapyId}")
    public ResponseEntity<TherapyResponse> cancel(JwtAuthenticationToken token, @PathVariable String therapyId) {
        User user = userService.validate(token);
        return ResponseEntity.ok(therapyService.cancel(user, therapyId));
    }
}
