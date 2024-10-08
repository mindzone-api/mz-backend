package com.mindzone.controller;


import com.mindzone.dto.request.UserRequest;
import com.mindzone.dto.response.UserResponse;
import com.mindzone.model.user.User;
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
    public ResponseEntity<UserResponse> signUp(JwtAuthenticationToken token, @RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(userService.signUp(token, userRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(JwtAuthenticationToken token, @PathVariable String id) {
        userService.validate(token);
        return ResponseEntity.ok(userService.get(id));
    }

    @GetMapping
    public ResponseEntity<UserResponse> whoAmI(JwtAuthenticationToken token) {
        User user = userService.validate(token);
        return ResponseEntity.ok(userService.whoAmI(user));
    }

}
