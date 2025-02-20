package com.example.tech_store.controller;

import com.example.tech_store.DTO.request.OrderRequestDTO;
import com.example.tech_store.DTO.response.ApiResponseDTO;
import com.example.tech_store.model.Order;
import com.example.tech_store.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<Order>> createOrder(@RequestBody OrderRequestDTO orderRequest) {
        Order order = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(ApiResponseDTO.<Order>
                builder()
                .success(true)
                .message("order created")
                .status(200)
                .timestamp(new Date())
                .data(order)
                .build());
    }
}
