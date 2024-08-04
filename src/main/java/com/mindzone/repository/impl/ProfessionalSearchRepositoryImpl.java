package com.mindzone.repository.impl;

import com.mindzone.dto.request.SearchFilter;
import com.mindzone.model.user.User;
import com.mindzone.repository.ProfessionalSearchRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class ProfessionalSearchRepositoryImpl  implements ProfessionalSearchRepository {

    private MongoTemplate mongoTemplate;

    @Override
    public Page<User> search(SearchFilter filter) {
        // Create custom query with pagination
        Query query = applyFilters(filter);

        // apply pagination
        int page = filter.getPage() != null ? filter.getPage() : 0;
        int size = filter.getSize() != null ? filter.getSize() : 10;
        long total = mongoTemplate.count(query, User.class);
        Pageable pageable = PageRequest.of(page, size);
        query.with(pageable);

        // run query
        List<User> users = mongoTemplate.find(query, User.class);

        // return results with pagination context
        return new PageImpl<>(users, pageable, total);
    }

    private Query applyFilters(SearchFilter filter) {
        Query query = new Query();
        filter.getList().forEach(field -> field.apply(query));

        return query;
    }
}
