package com.example.orderservice.application.service;

import com.example.orderservice.domain.entity.OrderEntity;
import com.example.orderservice.presentation.dto.OrderDto;
import com.example.orderservice.presentation.dto.request.OrderRequest;
import com.example.orderservice.presentation.dto.response.OrderResponse;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    OrderResponse createOrder(String userId, OrderRequest orderRequest);
    OrderDto getOrderByOrderId(String orderId);
    List<OrderResponse> getOrdersByUserId(String userId);

}