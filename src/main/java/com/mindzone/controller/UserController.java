package com.mindzone.controller;

import com.mindzone.model.User;
import com.mindzone.repository.UserRepository;
import com.mindzone.service.MailServiceImpl;
import com.mindzone.service.impl.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import static com.mindzone.util.Constants.V1;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(V1 + "/user")
public class UserController {

    @GetMapping
    public String getUserInfo(JwtAuthenticationToken principal) {
        return (String) principal.getTokenAttributes().get("email");
    }

    @GetMapping("/test")
    public Principal test(Principal principal) {
        return principal;
    }


}
