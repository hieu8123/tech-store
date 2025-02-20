package com.example.tech_store.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentMethod {
    CASH_ON_DELIVERY("cash on delivery"),  // Thanh toán khi nhận hàng
    CREDIT_CARD("credit_cart"),       // Thanh toán bằng thẻ tín dụng
    PAYPAL("paypal"),            // Thanh toán qua PayPal
    BANK_TRANSFER("bank_transfer"),     // Chuyển khoản ngân hàng
    MOMO("momo"),              // Thanh toán qua MoMo
    VNPAY("vnpay");              // Thanh toán qua VNPay

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PaymentMethod fromValue(String value) {
        for (PaymentMethod method : values()) {
            if (method.value.equalsIgnoreCase(value)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Unexpected payment method value: " + value);
    }
}
