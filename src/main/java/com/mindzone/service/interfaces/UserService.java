package com.mindzone.service.interfaces;

import com.mindzone.dto.request.SearchFilter;
import com.mindzone.dto.request.UserRequest;
import com.mindzone.dto.response.listed.ListedProfessional;
import com.mindzone.dto.response.UserResponse;
import com.mindzone.enums.Role;
import com.mindzone.model.therapy.Therapy;
import com.mindzone.model.user.User;
import com.mindzone.model.user.WeekDaySchedule;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;

public interface UserService {
    
    User validate(JwtAuthenticationToken token);
    User validate(JwtAuthenticationToken token, Role role);
    UserResponse signUp(JwtAuthenticationToken token, UserRequest userRequest);
    UserResponse get(String id);

    List<WeekDaySchedule> getAgendaCopy(List<WeekDaySchedule> agenda);

    void validateFields(UserRequest request);

    boolean isAlly(User professional, Therapy therapy);

    Page<ListedProfessional> search(SearchFilter filter);
    User getById(String id);
    void save(User user);
    UserResponse whoAmI(User user);
}
