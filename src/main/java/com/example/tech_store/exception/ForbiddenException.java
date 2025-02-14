package com.example.tech_store.exception;

import org.springframework.security.core.AuthenticationException;

public class ForbiddenException extends AuthenticationException {
    public ForbiddenException(String message) {
        super(message);
    }
}
