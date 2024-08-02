package com.mindzone.controller;


import com.mindzone.dto.request.SignUpRequest;
import com.mindzone.dto.response.SignUpResponse;
import com.mindzone.service.impl.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mindzone.util.Constants.V1;

@RestController
@AllArgsConstructor
@RequestMapping(V1 + "/user")
public class UserController {

    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(userService.signUp(signUpRequest));
    }

}
