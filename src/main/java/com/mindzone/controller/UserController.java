package com.mindzone.controller;

import com.mindzone.model.User;
import com.mindzone.repository.UserRepository;
import com.mindzone.service.MailServiceImpl;
import com.mindzone.service.impl.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class UserController {

    private UserService userService;
    private UserRepository userRepository;

    private MailServiceImpl mailService;

    @PostMapping("/users")
    public User postUser() {
        return userRepository.save(new User("Vinícius", 22));
    }

    @GetMapping("/users")
    public User getUsers() throws InterruptedException {
        return userService.getUsers();
    }

    @PostMapping("/sendMail")
    public String sendMail() {
        return mailService.sendMail();
    }

}
