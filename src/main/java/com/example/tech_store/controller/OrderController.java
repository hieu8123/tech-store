package com.example.tech_store.controller;

import com.example.tech_store.DTO.request.OrderRequestDTO;
import com.example.tech_store.DTO.response.ApiResponseDTO;
import com.example.tech_store.enums.OrderStatus;
import com.example.tech_store.model.Order;
import com.example.tech_store.services.OrderService;
import com.example.tech_store.utils.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<String>> createOrder(@RequestBody OrderRequestDTO orderRequest, HttpServletRequest request) {
        String clientIp = RequestUtil.getIpAddress(request);
        String paymentUrl = orderService.createOrder(orderRequest, clientIp);
        return ResponseEntity.ok(ApiResponseDTO.<String>
                builder()
                .success(true)
                .message("order created")
                .status(200)
                .timestamp(new Date())
                .data(paymentUrl)
                .build());
    }

    @GetMapping
    public ResponseEntity<Page<Order>> getOrders(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String note,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.getOrders(userId, status, note, page, size));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable UUID orderId, @RequestBody OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable UUID orderId, @RequestBody OrderRequestDTO orderRequest) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, orderRequest));
    }
}
