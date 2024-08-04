package com.mindzone.model.user.filter;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@AllArgsConstructor
public class NeighborhoodFilter implements Filter{
    private String neighborhood;
    @Override
    public void apply(Query query) {
        if (neighborhood != null && !neighborhood.isEmpty()) {
            query.addCriteria(Criteria.where("neighborhood").regex("^" + neighborhood, "i"));
        }
    }
}
