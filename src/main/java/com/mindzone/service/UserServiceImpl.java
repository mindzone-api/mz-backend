package com.mindzone.service;

import com.mindzone.dto.request.SignUpRequest;
import com.mindzone.dto.response.SignUpResponse;
import com.mindzone.enums.Role;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.user.User;
import com.mindzone.repository.UserRepository;
import com.mindzone.service.impl.UserService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.mindzone.exception.ExceptionMessages.*;
import static com.mindzone.util.Constants.V1;

@Service
@AllArgsConstructor

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UltimateModelMapper m;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiRequestException(USER_NOT_FOUND));
    }

    public User validateUser(JwtAuthenticationToken token, Role role) {
        User user = getUser((String) token.getTokenAttributes().get("email"));
        if (user.getRole() != role) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
        }
        return user;
    }

    @Override
    public SignUpResponse signUp(SignUpRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ApiRequestException(USER_ALREADY_EXISTS);
        }
        User user = m.map(request, User.class);
        user.setRole(user.getProfessionalInfo() == null ? Role.PATIENT : Role.PROFESSIONAL);
        // TODO stripe integration
        this.userRepository.save(user);
        return m.map(user, SignUpResponse.class);
    }

}
