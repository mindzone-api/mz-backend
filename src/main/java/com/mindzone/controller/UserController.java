package com.mindzone.controller;


import com.mindzone.dto.request.SignUpRequest;
import com.mindzone.dto.response.SignUpResponse;
import com.mindzone.dto.response.UserResponse;
import com.mindzone.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static com.mindzone.constants.Constants.V1;

@RestController
@AllArgsConstructor
@RequestMapping(V1 + "/user")
public class UserController {

    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        return new ResponseEntity<>(userService.signUp(signUpRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(JwtAuthenticationToken token, @PathVariable String id) {
        userService.validateUser(token);
        return ResponseEntity.ok(userService.get(id));
    }

}
