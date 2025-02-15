package com.example.tech_store.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BannerStatus {
    ACTIVE("active"),
    INACTIVE("inactive");

    private final String value;

    BannerStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static BannerStatus fromValue(String value) {
        for (BannerStatus status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unexpected banner status value: " + value);
    }
}
