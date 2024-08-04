package com.mindzone.model.user.filter;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;

import static com.mindzone.util.DateUtil.calculateBirthFromMaximumAge;
import static com.mindzone.util.DateUtil.calculateBirthFromMinimumAge;


@AllArgsConstructor
public class AgeFilter implements Filter{
    private Integer minimumAge;
    private Integer maximumAge;

    @Override
    public void apply(Query query) {
        if (minimumAge != null || maximumAge != null) {
            Criteria ageCriteria = Criteria.where("birthDate");
            if (minimumAge != null) {
                Date minimumBirthDateLimit = calculateBirthFromMinimumAge(minimumAge);
                ageCriteria = ageCriteria.lte(minimumBirthDateLimit);
            }
            if (maximumAge != null) {
                Date maximumBirthDateLimit = calculateBirthFromMaximumAge(maximumAge);
                ageCriteria = ageCriteria.gte(maximumBirthDateLimit);
            }
            query.addCriteria(ageCriteria);
        }
    }
}
