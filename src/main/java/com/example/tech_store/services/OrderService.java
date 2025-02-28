package com.example.tech_store.services;

import com.example.tech_store.DTO.request.OrderRequestDTO;
import com.example.tech_store.enums.OrderStatus;
import com.example.tech_store.kafka.producer.OrderProducer;
import com.example.tech_store.model.*;
import com.example.tech_store.repository.*;
import com.example.tech_store.services.payment.PaymentFactory;
import com.example.tech_store.services.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final OrderProducer orderProducer;
    private final ProductRepository productRepository;
    private final PaymentFactory paymentFactory;

    @Transactional
    public String createOrder(OrderRequestDTO orderRequest, String clientIp) {

        User user = userRepository.findById(orderRequest.getUserId()).get();

        Order order = Order.builder()
                .user(user)
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

        return paymentService.processPayment(order, clientIp);
    }

    public Page<Order> getOrders(UUID userId, OrderStatus status, String note, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        if (userId != null && status != null) {
            return orderRepository.findByUserIdAndStatus(userId, status, pageable);
        } else if (userId != null) {
            return orderRepository.findByUserId(userId, pageable);
        } else if (status != null) {
            return orderRepository.findByStatus(status, pageable);
        } else if (note != null && !note.isEmpty()) {
            return orderRepository.findByNoteContainingIgnoreCase(note, pageable);
        } else {
            return orderRepository.findAll(pageable);
        }
    }

    @Transactional
    public Order updateOrder(UUID orderId, OrderRequestDTO orderRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (orderRequest.getNote() != null) {
            order.setNote(orderRequest.getNote());
        }
        order.setTotal(Optional.of(orderRequest.getTotal()).orElse(order.getTotal()));

        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrderStatus(UUID orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);




        return orderRepository.save(order);
    }

    public boolean checkOrderInfo(UUID orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new RuntimeException("Order not found for orderId: " + orderId);
        }
        if (orderDetails.isEmpty()) {
            throw new RuntimeException("No order details found for orderId: " + orderId);
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
