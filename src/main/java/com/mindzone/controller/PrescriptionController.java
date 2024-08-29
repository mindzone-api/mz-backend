package com.mindzone.controller;

import com.mindzone.dto.request.PrescritionRequest;
import com.mindzone.dto.response.PrescriptionResponse;
import com.mindzone.model.user.User;
import com.mindzone.service.interfaces.PrescriptionService;
import com.mindzone.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static com.mindzone.constants.Constants.V1;
import static com.mindzone.enums.Profession.PSYCHIATRIST;

@RestController
@AllArgsConstructor
@RequestMapping(V1 + "/prescription")
public class PrescriptionController {

    private UserService userService;
    private PrescriptionService prescriptionService;

    @PostMapping
    public ResponseEntity<PrescriptionResponse> create(JwtAuthenticationToken token, @RequestBody PrescritionRequest request) {
        User psychiatrist = userService.validate(token, PSYCHIATRIST);
        return ResponseEntity.ok(prescriptionService.create(psychiatrist, request));
    }

    @GetMapping("/{prescriptionId}")
    public ResponseEntity<PrescriptionResponse> get(JwtAuthenticationToken token, @PathVariable String prescriptionId) {
        User user = userService.validate(token);
        return ResponseEntity.ok(prescriptionService.get(user, prescriptionId));
    }

}
