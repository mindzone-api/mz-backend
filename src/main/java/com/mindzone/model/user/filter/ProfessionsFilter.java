package com.mindzone.model.user.filter;

import com.mindzone.enums.Profession;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@AllArgsConstructor
public class ProfessionsFilter implements Filter {
    private List<Profession> professions;

    @Override
    public void apply(Query query) {
        if (professions != null && !professions.isEmpty()) {
            query.addCriteria(Criteria.where("professionalInfo.profession").in(professions));
        }
    }
}

