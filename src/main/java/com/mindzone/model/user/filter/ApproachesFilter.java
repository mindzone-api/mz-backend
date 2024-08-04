package com.mindzone.model.user.filter;

import com.mindzone.enums.Approach;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@AllArgsConstructor
public class ApproachesFilter implements Filter{
    private List<Approach> approaches;
    @Override
    public void apply(Query query) {
        if (approaches != null && !approaches.isEmpty()) {
            query.addCriteria(Criteria.where("professionalInfo.approach").in(approaches));
        }
    }
}
