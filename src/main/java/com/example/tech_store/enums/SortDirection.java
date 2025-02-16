package com.example.tech_store.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SortDirection {
    ASC("asc"),
    DESC("desc");

    private final String value;

    SortDirection(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static SortDirection fromValue(String value) {
        for (SortDirection sortDirection : values()) {
            if (sortDirection.value.equalsIgnoreCase(value)) {
                return sortDirection;
            }
        }
        throw new IllegalArgumentException("Unexpected order status value: " + value);
    }
}
