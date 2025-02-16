package com.example.tech_store.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CategorySortField {
    NAME("name"),
    CREATED_AT("createdAt"),
    ID("id");
    private final String value;

    CategorySortField(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static CategorySortField fromValue(String value) {
        for (CategorySortField categorySortField : values()) {
            if (categorySortField.value.equalsIgnoreCase(value)) {
                return categorySortField;
            }
        }
        throw new IllegalArgumentException("Unexpected order status value: " + value);
    }
}
