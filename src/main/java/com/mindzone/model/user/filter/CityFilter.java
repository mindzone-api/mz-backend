package com.mindzone.model.user.filter;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@AllArgsConstructor
public class CityFilter implements Filter {
    private String city;
    @Override
    public void apply(Query query) {
        if (city != null && !city.isEmpty()) {
            query.addCriteria(Criteria.where("city").regex("^" + city, "i"));
        }
    }
}
