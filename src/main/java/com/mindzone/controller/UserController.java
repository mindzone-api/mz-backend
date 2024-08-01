package com.mindzone.controller;

import com.mindzone.model.User;
import com.mindzone.repository.UserRepository;
import com.mindzone.service.MailServiceImpl;
import com.mindzone.service.impl.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

import static com.mindzone.util.Constants.V1;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(V1 + "/user")
public class UserController {

    @GetMapping
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

}
