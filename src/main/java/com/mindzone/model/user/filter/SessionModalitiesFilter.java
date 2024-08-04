package com.mindzone.model.user.filter;

import com.mindzone.enums.SessionModality;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@AllArgsConstructor
public class SessionModalitiesFilter implements Filter{
    private List<SessionModality> sessionModalities;

    @Override
    public void apply(Query query) {
        if (sessionModalities != null && !sessionModalities.isEmpty()) {
            query.addCriteria(Criteria.where("professionalInfo.sessionModalities").in(sessionModalities));
        }
    }
}
