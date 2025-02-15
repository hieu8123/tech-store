package com.example.tech_store.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
    PENDING("pending"),
    PROCESSING("processing"),
    SHIPPED("shipped"),
    DELIVERED("delivered"),
    CANCELLED("cancelled"),
    ONLINE_PAYMENT("online_payment"),
    OFFLINE_PAYMENT("offline_payment"),
    FAILED_PAYMENT("failed_payment");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static OrderStatus fromValue(String value) {
        for (OrderStatus status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unexpected order status value: " + value);
    }
}
