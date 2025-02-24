package com.example.tech_store.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;

@Configuration
@Getter
public class VNPayConfig {

    @Value("${vnpay.base-url}")
    private String baseUrl;

    @Value("${vnpay.return-url}")
    private String returnUrl;

    @Value("${vnpay.tmn-code}")
    private String tmnCode;

    @Value("${vnpay.hash-secret}")
    private String hashSecret;

    @Value("${vnpay.version}")
    private String version;

    @Value("${vnpay.command}")
    private String command;

    @Value("${vnpay.currency-code}")
    private String currencyCode;

    @Value("${vnpay.locale}")
    private String locale;
}
