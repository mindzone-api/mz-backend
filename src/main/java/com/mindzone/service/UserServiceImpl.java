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

import java.util.Date;

import static com.mindzone.exception.ExceptionMessages.*;

@Service
@AllArgsConstructor

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UltimateModelMapper m;

    private void save(User user) {
        user.setUpdatedAt(new Date());
        this.userRepository.save(user);
    }

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
        if (
                request.getProfessionalInfo() != null &&
                userRepository.findByProfessionalInfoProfessionalCode(
                        request.getProfessionalInfo().getProfessionalCode()
                ).isPresent()
        ) {
            throw new ApiRequestException(PROFESSIONAL_CODE_ALREADY_EXISTS);
        }

        User user = m.map(request, User.class);
        user.setRole(user.getProfessionalInfo() == null ? Role.PATIENT : Role.PROFESSIONAL);
        user.setCreatedAt(new Date());
        // TODO stripe integration
        save(user);
        return m.map(user, SignUpResponse.class);
    }

}
