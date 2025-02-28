package com.example.tech_store.constants;

import org.springframework.http.HttpMethod;

import java.util.Map;

public class ApiConstants {
    public static final String BASE_URL = "/api";

    public static final String API_VERSION = "/v1";
    public static final String BASE_API_PATH = BASE_URL + API_VERSION;

    public static class InterceptorPatterns {
        public static final String[] USER_PATHS = {
                BASE_API_PATH + "/users/{id}/**",
        };
    }

    public static class Cors {
        public static final String[] ALLOWED_ORIGINS = {
                "http://localhost:3000",
                "http://localhost:8080",
                "https://your-production-frontend.com"
        };

        public static final String[] ALLOWED_METHODS = {
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        };

        public static final String[] ALLOWED_HEADERS = {
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        };

        public static final String[] EXPOSED_HEADERS = {
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"
        };

        public static final long MAX_AGE = 3600L; // 1 hour
    }

    // API Endpoints
    public static class Endpoints {
        public static final String AUTH = BASE_API_PATH + "/auth";
        public static final String CATEGORIES = BASE_API_PATH + "/categories";
        public static final String USERS = BASE_API_PATH + "/users";
        public static final String PRODUCTS = BASE_API_PATH + "/products";
        public static final String ADMIN = BASE_API_PATH + "/admin";


        public static final Map<String, Map<HttpMethod, String[]>> ROLE_PERMISSIONS = Map.of(
                "ADMIN", Map.of(
                        HttpMethod.DELETE, new String[]{CATEGORIES, USERS, PRODUCTS,ADMIN},
                        HttpMethod.PUT, new String[]{CATEGORIES, USERS, PRODUCTS, ADMIN},
                        HttpMethod.POST,new String[]{CATEGORIES, USERS, PRODUCTS, ADMIN},
                        HttpMethod.GET, new String[]{USERS, ADMIN}
                ),
                "USER", Map.of(
                        HttpMethod.GET, new String[]{USERS}
                )
        );
    }

    private ApiConstants() {
    }
}