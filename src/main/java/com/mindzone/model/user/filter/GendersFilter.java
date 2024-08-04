package com.mindzone.model.user.filter;

import com.mindzone.enums.Gender;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@AllArgsConstructor
public class GendersFilter implements Filter {
    private List<Gender> genders;
    @Override
    public void apply(Query query) {
        if (genders != null && !genders.isEmpty()) {
            query.addCriteria(Criteria.where("gender").in(genders));
        }
    }
}
