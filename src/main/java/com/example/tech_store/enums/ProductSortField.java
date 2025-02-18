package com.example.tech_store.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProductSortField {
    NAME("name"),
    CREATED_AT("createdAt"),
    ID("id"),
    PRICE("price");
    private final String value;

    ProductSortField(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ProductSortField fromValue(String value) {
        for (ProductSortField productSortField : values()) {
            if (productSortField.value.equalsIgnoreCase(value)) {
                return productSortField;
            }
        }
        throw new IllegalArgumentException("Unexpected product sort field value: " + value);
    }
}
