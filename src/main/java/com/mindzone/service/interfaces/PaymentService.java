package com.mindzone.service.interfaces;

import com.mindzone.dto.request.StripeClientRequest;
import com.stripe.exception.StripeException;

public interface PaymentService {
    void createCustomer(StripeClientRequest stripeClientRequest) throws StripeException;

    void updateCustomer(StripeClientRequest s);

    String createSubscription(String customerId);

    void cancelSubscription(String subscriptionId);
}
