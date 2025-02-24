package com.example.tech_store.services;

import com.example.tech_store.DTO.request.OrderRequestDTO;
import com.example.tech_store.enums.OrderStatus;
import com.example.tech_store.enums.PaymentStatus;
import com.example.tech_store.exception.InvalidDataException;
import com.example.tech_store.kafka.producer.OrderProducer;
import com.example.tech_store.model.*;
import com.example.tech_store.repository.*;
import com.example.tech_store.services.payment.PaymentFactory;
import com.example.tech_store.services.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final OrderProducer orderProducer;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final PaymentFactory paymentFactory;

    @Transactional
    public String createOrder(OrderRequestDTO orderRequest) {
        Order order = Order.builder()
                .user(User.builder().id(orderRequest.getUserId()).build())
                .status(OrderStatus.PENDING)
                .note(orderRequest.getNote())
                .total(orderRequest.getTotal())
                .build();

        final Order savedOrder = orderRepository.save(order);

        List<OrderDetail> orderDetails = orderRequest.getOrderDetails().stream()
                .map(item -> OrderDetail.builder()
                        .order(order)
                        .product(Product.builder().id(item.getProductId()).build())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());
        orderDetailRepository.saveAll(orderDetails);

        orderProducer.sendInventoryCheckEvent(savedOrder.getId());

        PaymentService paymentService = paymentFactory.getPaymentService(orderRequest.getPaymentMethod());

        return paymentService.processPayment(order);
    }

    public boolean checkProductStock(UUID orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        if (orderDetails.isEmpty()) {
            throw new InvalidDataException("No order details found for orderId: " + orderId);
        }

        for (OrderDetail item : orderDetails) {
            Product product = productRepository.findById(item.getProduct().getId()).orElse(null);

            if (product == null || product.getQuantity() < item.getQuantity()) {
                return false;
            }
        }

        return true;
    }


    public void cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
