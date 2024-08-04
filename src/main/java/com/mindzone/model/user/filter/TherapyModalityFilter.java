package com.mindzone.model.user.filter;

import com.mindzone.enums.TherapyModality;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@AllArgsConstructor
public class TherapyModalityFilter implements Filter{
    private List<TherapyModality> therapyModalities;

    @Override
    public void apply(Query query) {
        if (therapyModalities != null && !therapyModalities.isEmpty()) {
            query.addCriteria(Criteria.where("professionalInfo.therapyModalities").in(therapyModalities));
        }
    }
}
