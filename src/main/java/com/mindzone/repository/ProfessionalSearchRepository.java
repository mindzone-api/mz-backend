package com.mindzone.repository;

import com.mindzone.dto.request.SearchFilter;
import com.mindzone.dto.response.ListedProfessional;
import org.springframework.data.domain.Page;

public interface ProfessionalSearchRepository {

    Page<ListedProfessional> search(SearchFilter filter);

}
