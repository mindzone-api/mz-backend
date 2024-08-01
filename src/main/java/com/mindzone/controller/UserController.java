package com.mindzone.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

import static com.mindzone.util.Constants.V1;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(V1 + "/user")
public class UserController {

    @GetMapping("/email")
    public String getUserEmail(JwtAuthenticationToken jwtAuth) {
        return (String) jwtAuth.getTokenAttributes().get("email");
    }

    @GetMapping("/test")
    public Principal test(Principal principal) {
        return principal;
    }


}
