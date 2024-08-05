package com.mindzone.service.interfaces;

import com.mindzone.dto.request.SearchFilter;
import com.mindzone.dto.request.SignUpRequest;
import com.mindzone.dto.response.ListedProfessional;
import com.mindzone.dto.response.SignUpResponse;
import com.mindzone.dto.response.UserResponse;
import com.mindzone.enums.Role;
import com.mindzone.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;

public interface UserService {
    
    User validateUser(JwtAuthenticationToken token);

    User validateUser(JwtAuthenticationToken token, Role role);

    SignUpResponse signUp(SignUpRequest signUpRequest);

    UserResponse get(String id);

    Page<ListedProfessional> search(SearchFilter filter);

    User getById(String id);
}
