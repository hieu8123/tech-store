package com.example.tech_store.controller;

import com.example.tech_store.services.payment.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final VNPayService vnPayService;

    @GetMapping("/vnpay/callback")
    public ResponseEntity<String> handleVNPayCallback(HttpServletRequest request) {
        int result = vnPayService.handleCallback(request);
        if (result == 1) {
            return ResponseEntity.ok("Payment processed successfully!");
        } else if (result == 0) {
            return ResponseEntity.badRequest().body("Payment failed!");
        } else {
            return ResponseEntity.badRequest().body("Invalid signature or payment not found!");
        }
    }
}
