package com.mindzone.service.interfaces;

import com.mindzone.dto.request.StripeClientRequest;
import com.stripe.exception.StripeException;

public interface PaymentService {
    String createSubscription(String customerId) throws StripeException;

    String createSession() throws StripeException;
}
