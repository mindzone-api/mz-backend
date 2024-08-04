package com.mindzone.model.user.filter;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;

@AllArgsConstructor
public class PriceFilter implements Filter {
    private BigDecimal minimumPrice;
    private BigDecimal maximumPrice;

    @Override
    public void apply(Query query) {
        if (minimumPrice != null || maximumPrice != null) {
            Criteria priceCriteria = Criteria.where("professionalInfo.pricePerSession");
            if (minimumPrice != null) {
                priceCriteria = priceCriteria.gte(minimumPrice);
            }
            if (maximumPrice != null) {
                priceCriteria = priceCriteria.lte(maximumPrice);
            }
            query.addCriteria(priceCriteria);
        }
    }
}
