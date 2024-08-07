package com.mindzone.service.impl;

import com.mindzone.dto.request.StripeClientRequest;
import com.mindzone.service.interfaces.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.mindzone.constants.Constants.STRIPE_API_KEY;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Override
    public void createCustomer(StripeClientRequest s) throws StripeException {
        // https://dashboard.stripe.com/apikeys
        Stripe.apiKey = STRIPE_API_KEY;

        CustomerCreateParams params =
                CustomerCreateParams.builder()
                        .setName(s.getName())
                        .setEmail(s.getEmail())
                        .build();

        Customer.create(params);
    }

    @Override
    public void updateCustomer(StripeClientRequest s) {
        Stripe.apiKey = STRIPE_API_KEY;
    }

    @Override
    public String createSubscription(String customerId) {
        Stripe.apiKey = STRIPE_API_KEY;
        return null;
    }

    @Override
    public void cancelSubscription(String customerId) {
        Stripe.apiKey = STRIPE_API_KEY;

    }
}
