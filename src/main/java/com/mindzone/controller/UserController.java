package com.mindzone.controller;

import com.mindzone.model.User;
import com.mindzone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {

    private UserRepository userRepository;

    @PostMapping("/users")
    public User postUser() {
        return userRepository.save(new User("Vinícius", 22));
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

}
