package com.mindzone.model.user.filter;

import com.mindzone.enums.AvailabilitySearchType;
import com.mindzone.model.user.TimeRange;
import com.mindzone.model.user.WeekDayAvailability;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class AvailabilityFilter implements Filter{
    private List<WeekDayAvailability> availability;
    private AvailabilitySearchType availabilitySearchType;

    @Override
    public void apply(Query query) {
        if (availability != null && !availability.isEmpty()) {
            List<Criteria> criteriaList = new ArrayList<>();
            for (WeekDayAvailability filterAvailability : availability) {
                for (TimeRange filterTimeRange : filterAvailability.getDaySchedule()) {
                    criteriaList.add(
                            Criteria.where("professionalInfo.availability")
                                    .elemMatch(Criteria.where("day").is(filterAvailability.getDay())
                                            .andOperator(
                                                    Criteria.where("daySchedule")
                                                            .elemMatch(Criteria.where("startsAt").gte(filterTimeRange.getStartsAt())
                                                                    .and("endsAt").lte(filterTimeRange.getEndsAt()))
                                        )
                                )
                    );
                }
            }

            Criteria criteria =
                    availabilitySearchType == AvailabilitySearchType.ANY ?
                            new Criteria().orOperator(criteriaList) :
                            new Criteria().andOperator(criteriaList);
            query.addCriteria(criteria);
        }
    }

}
