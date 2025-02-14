package com.example.tech_store.config;

import java.util.List;
import java.util.Arrays;

public final class SecurityConstants {
    private SecurityConstants() {}

    public static final class Endpoints {
        private Endpoints() {}

        public static final String[] ADMIN_ENDPOINTS = {
                "/api/admin/**",
                "/api/test/admin/**"
        };

        public static final String[] PRIVATE_ENDPOINTS = {
                "/api/private/**",
                "/api/test/private/**"
        };
    }

    public static final class Cors {
        private Cors() {}

        public static final List<String> ALLOWED_ORIGINS = List.of("*");
        public static final List<String> ALLOWED_METHODS = Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        );
        public static final List<String> ALLOWED_HEADERS = List.of("*");
    }
}