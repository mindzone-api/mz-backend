package com.mindzone.repository;

import com.mindzone.dto.request.SearchFilter;
import com.mindzone.model.user.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProfessionalSearchRepository {

    Page<User> search(SearchFilter filter);

}
