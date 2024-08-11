package com.mindzone.service.interfaces;

import com.mindzone.dto.request.SearchFilter;
import com.mindzone.dto.request.UserRequest;
import com.mindzone.dto.response.listed.ListedProfessional;
import com.mindzone.dto.response.UserResponse;
import com.mindzone.enums.Role;
import com.mindzone.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public interface UserService {
    
    User validateUser(JwtAuthenticationToken token);

    User validateUser(JwtAuthenticationToken token, Role role);

    UserResponse signUp(UserRequest userRequest);

    UserResponse get(String id);

    Page<ListedProfessional> search(SearchFilter filter);

    User getById(String id);

    void save(User user);

    UserResponse update(String id, UserRequest userRequest);
}
