package com.mindzone.service;

import com.mindzone.model.User;
import com.mindzone.repository.UserRepository;
import com.mindzone.service.impl.UserService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Cacheable("user")
    public User getUsers() throws InterruptedException {
        Thread.sleep(1000);
        return userRepository.findAll().get(0);
    }

}
