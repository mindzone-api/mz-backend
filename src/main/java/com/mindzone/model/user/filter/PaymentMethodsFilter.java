package com.mindzone.model.user.filter;

import com.mindzone.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@AllArgsConstructor
public class PaymentMethodsFilter implements Filter {
    private List<PaymentMethod> paymentMethods;
    @Override
    public void apply(Query query) {
        if (paymentMethods != null && !paymentMethods.isEmpty()) {
            query.addCriteria(Criteria.where("professionalInfo.paymentMethods").in(paymentMethods));
        }
    }
}
