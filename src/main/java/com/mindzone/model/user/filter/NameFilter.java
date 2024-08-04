package com.mindzone.model.user.filter;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@AllArgsConstructor
public class NameFilter implements Filter {
    private String name;

    @Override
    public void apply(Query query) {
        if (name != null && !name.isEmpty()) {
            query.addCriteria(Criteria.where("name").regex("^" + name, "i"));
        }
    }
}
