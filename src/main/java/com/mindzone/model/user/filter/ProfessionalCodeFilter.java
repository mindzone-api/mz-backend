package com.mindzone.model.user.filter;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@AllArgsConstructor
public class ProfessionalCodeFilter implements Filter {
    private String professionalCode;

    @Override
    public void apply(Query query) {
        if (professionalCode != null && !professionalCode.isEmpty()) {
            query.addCriteria(Criteria.where("professionalInfo.professionalCode").regex("^" + professionalCode));
        }
    }
}
