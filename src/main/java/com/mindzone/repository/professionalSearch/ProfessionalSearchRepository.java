package com.mindzone.repository.professionalSearch;

import com.mindzone.dto.request.SearchFilter;
import com.mindzone.dto.response.ListedProfessional;
import org.springframework.data.domain.Page;

public interface ProfessionalSearchRepository {

    Page<ListedProfessional> search(SearchFilter filter);

}
