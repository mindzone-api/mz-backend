package com.mindzone.controller;

import com.mindzone.dto.request.SearchFilter;
import com.mindzone.model.user.User;
import com.mindzone.service.impl.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mindzone.constants.Constants.V1;

@RestController
@AllArgsConstructor
@RequestMapping(V1 + "/patient")
public class PatientController {

    private UserService userService;
    @GetMapping("/search")
    public ResponseEntity<Page<User>> search(JwtAuthenticationToken token, @RequestBody SearchFilter filter) {
        userService.validateUser(token);
        return ResponseEntity.ok(userService.search(filter));
    }
}
