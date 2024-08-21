package com.mindzone.service.impl;

import com.mindzone.dto.request.SearchFilter;
import com.mindzone.dto.request.UserRequest;
import com.mindzone.dto.response.listed.ListedProfessional;
import com.mindzone.dto.response.UserResponse;
import com.mindzone.enums.Role;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.user.User;
import com.mindzone.model.user.WeekDaySchedule;
import com.mindzone.repository.professionalSearch.ProfessionalSearchRepository;
import com.mindzone.repository.UserRepository;
import com.mindzone.service.interfaces.UserService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

import static com.mindzone.constants.Constants.EMPTY;
import static com.mindzone.enums.Role.PATIENT;
import static com.mindzone.enums.Role.PROFESSIONAL;
import static com.mindzone.exception.ExceptionMessages.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ProfessionalSearchRepository search;
    private UltimateModelMapper m;

    public void save(User user) {
        user.setUpdatedAt(new Date());
        this.userRepository.save(user);
    }

    public User getById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(USER_NOT_FOUND));
    }

    @Override
    public UserResponse whoAmI(User user) {
        return m.map(user, UserResponse.class);
    }

    @Override
    public UserResponse get(String id) {
        User user = getById(id);
        return m.map(user, UserResponse.class);
    }

    @Override
    public User validateUser(JwtAuthenticationToken token) {
        return userRepository.findByEmail((String) token.getTokenAttributes().get("email"))
                .orElseThrow(() -> new ApiRequestException(OAUTH_USER_NOT_FOUND));
    }

    public User validateUser(JwtAuthenticationToken token, Role role) {
        User user = validateUser(token);
        if (user.getRole() != role) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
        }
        return user;
    }

    @Override
    public UserResponse signUp(JwtAuthenticationToken token, UserRequest request) {
        validateFields(request);
        if (userRepository.findByEmail((String) token.getTokenAttributes().get("email")).isPresent()) {
            throw new ApiRequestException(USER_ALREADY_EXISTS);
        }

        User user = m.map(request, User.class);
        user.setName((String) token.getTokenAttributes().get("name"));
        user.setProfilePictureURL((String) token.getTokenAttributes().get("picture"));
        user.setEmail((String) token.getTokenAttributes().get("email"));
        user.setRole(user.getProfessionalInfo() == EMPTY ? PATIENT : PROFESSIONAL);
        user.setCreatedAt(new Date());

        if (user.getRole() == PROFESSIONAL) {
            user.getProfessionalInfo().setAvailability(getAgendaCopy(user.getProfessionalInfo().getAgenda()));
        }

        save(user);
        return m.map(user, UserResponse.class);
    }

    @Override
    public List<WeekDaySchedule> getAgendaCopy(List<WeekDaySchedule> agenda) {
        return agenda.stream()
                .map(WeekDaySchedule::new).toList();
    }
    @Override
    public void validateFields(UserRequest request) {
        if (
                request.getProfessionalInfo() != null &&
                        userRepository.findByProfessionalInfoProfessionalCode(
                                request.getProfessionalInfo().getProfessionalCode()
                        ).isPresent()
        ) {
            throw new ApiRequestException(PROFESSIONAL_CODE_ALREADY_EXISTS);
        }
    }

    @Override
//    @Cacheable("user") FIXME
    public Page<ListedProfessional> search(SearchFilter filter) {
        return search.search(filter);
    }
}
