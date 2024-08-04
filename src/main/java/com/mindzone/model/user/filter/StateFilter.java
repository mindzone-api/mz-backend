package com.mindzone.model.user.filter;

import com.mindzone.enums.State;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@AllArgsConstructor
public class StateFilter implements Filter{
    private State state;
    @Override
    public void apply(Query query) {
        if (state != null) {
            query.addCriteria(Criteria.where("state").is(state));
        }
    }
}
