package com.example.tech_store.services;

import com.example.tech_store.DTO.request.OrderRequestDTO;
import com.example.tech_store.enums.OrderStatus;
import com.example.tech_store.enums.PaymentStatus;
import com.example.tech_store.exception.InvalidDataException;
import com.example.tech_store.kafka.producer.OrderProducer;
import com.example.tech_store.model.*;
import com.example.tech_store.repository.*;
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

    @Transactional
    public Order createOrder(OrderRequestDTO orderRequest) {
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

        // Tạo payment (chưa xử lý thanh toán)
        Payment payment = Payment.builder()
                .order(order)
                .method(orderRequest.getPaymentMethod())
                .status(PaymentStatus.PENDING)
                .build();
        paymentRepository.save(payment);

        orderProducer.sendInventoryCheckEvent(order.getId());

        return order;
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
