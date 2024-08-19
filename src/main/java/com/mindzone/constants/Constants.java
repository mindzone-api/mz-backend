package com.mindzone.constants;

import com.mindzone.model.user.ProfessionalInfo;

public class Constants {

    // --------------- DOMAIN --------------- \\
    public static final String V1 = "/api/v1";
    public static final String MINDZONE_HOMEPAGE = "localhost:8080" + V1 + "/home";

    // --------------- RABBIT MQ --------------- \\
    public static final String RABBIT_MAIL_QUEUE = "mz.mail";
    public static final String RABBIT_MAIL_TOPIC_EXCHANGE = "mz.mail";
    public static final String RABBIT_MAIL_ROUTING_KEY = "mz.mail";

    // --------------- AUTHENTICATION --------------- \\
    public static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    public static final String CLIENT_ID = System.getenv("GOOGLE_CLIENT_ID");
    public static final String CLIENT_SECRET = System.getenv("GOOGLE_CLIENT_SECRET");
    public static final String[] WHITELIST_ENDPOINTS = {
            V1 + "/payment/**",
            "/error",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };

    // --------------- ROLES --------------- \\

    public static final ProfessionalInfo EMPTY = null;

    // --------------- PAYMENTS --------------- \\

    public static final String STRIPE_API_KEY = System.getenv("STRIPE_API_KEY");

    public static final String PRODUCT_ID = "prod_QcskfCJukFCWBw";

    public static final String PRICE_ID = "price_1PlcyuGYTtTKNlX9iuNZtSPC";

    public static final String SUCCESSFUL_PAYMENT_URL = "localhost:8080/api/v1/payment/success";

    public static final String FAILED_PAYMENT_URL = "localhost:8080/api/v1/payment/failure";

}
