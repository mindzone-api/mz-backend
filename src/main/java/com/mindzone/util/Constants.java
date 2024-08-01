package com.mindzone.util;

public class Constants {

    // CURRENT VERSION
    public static final String V1 = "/api/v1";

    // RABBIT MQ
    public static final String RABBIT_MAIL_QUEUE = "mz.mail";
    public static final String RABBIT_MAIL_TOPIC_EXCHANGE = "mz.mail";
    public static final String RABBIT_MAIL_ROUTING_KEY = "mz.mail";

    // OAUTH

    public static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    public static final String CLIENT_ID = System.getenv("GOOGLE_CLIENT_ID");
    public static final String CLIENT_SECRET = System.getenv("GOOGLE_CLIENT_SECRET");
    public static final String[] WHITE_LIST_URL = {
            V1 + "/auth/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };

}
