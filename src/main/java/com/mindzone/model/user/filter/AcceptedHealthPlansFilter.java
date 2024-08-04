package com.mindzone.model.user.filter;

import com.mindzone.enums.HealthPlan;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@AllArgsConstructor
public class AcceptedHealthPlansFilter implements Filter{
    private List<HealthPlan> acceptedHealthPlans;

    @Override
    public void apply(Query query) {
        if (acceptedHealthPlans != null && !acceptedHealthPlans.isEmpty()) {
            query.addCriteria(Criteria.where("professionalInfo.acceptedHealthPlans").in(acceptedHealthPlans));
        }
    }
}
