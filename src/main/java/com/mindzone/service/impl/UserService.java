package com.mindzone.service.impl;

import com.mindzone.dto.request.SignUpRequest;
import com.mindzone.dto.response.SignUpResponse;
import com.mindzone.dto.response.UserResponse;
import com.mindzone.enums.Role;
import com.mindzone.model.user.User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public interface UserService {
    
    User validateUser(JwtAuthenticationToken token);

    User validateUser(JwtAuthenticationToken token, Role role);

    SignUpResponse signUp(SignUpRequest signUpRequest);

    UserResponse get(String id);
}
