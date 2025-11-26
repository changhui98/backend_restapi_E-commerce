package com.example.orderservice.application.service.impl;

import com.example.orderservice.application.service.OrderService;
import com.example.orderservice.domain.entity.OrderEntity;
import com.example.orderservice.infrastructure.kafka.KafkaProducer;
import com.example.orderservice.infrastructure.kafka.OrderProducer;
import com.example.orderservice.infrastructure.repository.OrderJpaRepository;
import com.example.orderservice.presentation.dto.OrderDto;
import com.example.orderservice.presentation.dto.request.OrderRequest;
import com.example.orderservice.presentation.dto.response.OrderResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;

    @Override
    public OrderResponse createOrder(String userId, OrderRequest orderRequest) {

        OrderDto order = OrderDto.builder()
            .userId(userId)
            .productId(orderRequest.getProductId())
            .qty(orderRequest.getQty())
            .unitPrice(orderRequest.getUnitPrice())
            .orderId(UUID.randomUUID().toString())
            .totalPrice(orderRequest.getQty() * orderRequest.getUnitPrice())
            .build();

        OrderEntity orderEntity = OrderEntity.of(
            order.getProductId(),
            order.getQty(),
            order.getUnitPrice(),
            order.getTotalPrice(),
            order.getUserId(),
            order.getOrderId()
        );

        OrderEntity saveOrder = orderJpaRepository.save(orderEntity);

        return OrderResponse.fromEntity(saveOrder);
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {

        OrderEntity order = orderJpaRepository.findByOrderId(orderId);

        return OrderDto.builder()
            .userId(order.getUserId())
            .productId(order.getProductId())
            .qty(order.getQty())
            .unitPrice(order.getUnitPrice())
            .orderId(order.getOrderId())
            .totalPrice(order.getQty() * order.getUnitPrice())
            .build();
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(String userId) {

        List<OrderEntity> orderList = orderJpaRepository.findByUserId(userId);

        return orderList.stream()
            .map(OrderResponse::fromEntity)
            .toList();
    }
}
