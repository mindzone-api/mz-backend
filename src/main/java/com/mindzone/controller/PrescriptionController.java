package com.mindzone.controller;

import com.mindzone.dto.request.MzPageRequest;
import com.mindzone.dto.request.PrescritionRequest;
import com.mindzone.dto.response.PrescriptionResponse;
import com.mindzone.dto.response.listed.ListedPrescription;
import com.mindzone.model.user.User;
import com.mindzone.service.interfaces.PrescriptionService;
import com.mindzone.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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

    @PutMapping("/{prescriptionId}")
    public ResponseEntity<PrescriptionResponse> update(
            JwtAuthenticationToken token,
            @RequestBody PrescritionRequest request,
            @PathVariable String prescriptionId
    ) {
        User psychiatrist = userService.validate(token, PSYCHIATRIST);
        return ResponseEntity.ok(prescriptionService.update(psychiatrist, request, prescriptionId));
    }

    @GetMapping("/all/{therapyId}")
    public ResponseEntity<Page<ListedPrescription>> getAll(
            JwtAuthenticationToken token,
            @PathVariable String therapyId,
            @RequestBody MzPageRequest pageRequest
            ) {
        User user = userService.validate(token);
        return ResponseEntity.ok(prescriptionService.getAll(user, therapyId, pageRequest));
    }

    @DeleteMapping("/{prescriptionId}")
    public ResponseEntity<PrescriptionResponse> delete(JwtAuthenticationToken token, @PathVariable String prescriptionId) {
        User psychiatrist = userService.validate(token, PSYCHIATRIST);
        return ResponseEntity.ok(prescriptionService.delete(psychiatrist, prescriptionId));
    }
}
