package com.example.tech_store.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentStatus {
    PENDING("pending"),
    PAID("paid"),
    FAILED("failed"),
    REFUNDED("refunded");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PaymentStatus fromValue(String value) {
        for (PaymentStatus status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unexpected payment status value: " + value);
    }
}
