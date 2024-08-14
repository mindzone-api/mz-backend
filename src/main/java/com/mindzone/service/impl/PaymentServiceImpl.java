package com.mindzone.service.impl;

import com.mindzone.service.interfaces.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.*;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.mindzone.constants.Constants.*;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Override
    public String createSubscription(String customerId) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;

        SubscriptionCreateParams params =
                SubscriptionCreateParams.builder()
                        .setCustomer(customerId)
                        .addItem(
                                SubscriptionCreateParams.Item.builder().setPrice(PRODUCT_ID).build()
                        )
                        .build();

        Subscription subscription = Subscription.create(params);
        return subscription.getId();
    }

    public String createSession() throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;

        SessionCreateParams params = new SessionCreateParams.Builder()
                // this will only work with a frontend integration
                .setSuccessUrl(SUCCESSFUL_PAYMENT_URL)
                .setCancelUrl(FAILED_PAYMENT_URL)
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .addLineItem(new SessionCreateParams.LineItem.Builder()
                        .setQuantity(1L)
                        .setPrice(PRICE_ID)
                        .build()
                )
                .build();

        Session session = Session.create(params);

        return null;
    }
}