package com.example.tech_store.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
    PENDING("pending"),          // Đơn hàng đang chờ xác nhận
    PROCESSING("processing"),    // Đang xử lý
    SHIPPED("shipped"),          // Đã giao cho đơn vị vận chuyển
    DELIVERED("delivered"),      // Đã giao hàng thành công
    CANCELLED("cancelled");      // Đơn hàng bị hủy

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
