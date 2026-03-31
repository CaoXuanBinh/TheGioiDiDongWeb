package com.hutech.CAOXUANBINH.config;

import org.springframework.context.annotation.Configuration;
import lombok.Getter;

@Configuration
@Getter
public class MomoConfig {
    private final String partnerCode = "MOMO";
    private final String accessKey = System.getenv().getOrDefault("MOMO_ACCESS_KEY", "REPLACE_WITH_MOMO_ACCESS_KEY");
    private final String secretKey = System.getenv().getOrDefault("MOMO_SECRET_KEY", "REPLACE_WITH_MOMO_SECRET_KEY");

    // Gateway endpoint for POST
    private final String endpointUrl = "https://test-payment.momo.vn/v2/gateway/api/create";

    private final String returnUrl = "http://localhost:8080/momo/return";
    private final String notifyUrl = "http://localhost:8080/momo/notify"; // NGROK might be needed for real IPN, but
                                                                          // we'll handle standard logic.
}
