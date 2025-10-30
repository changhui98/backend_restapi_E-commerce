package com.example.orderservice.presentation.dto.response;

import com.example.orderservice.domain.entity.OrderEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {

    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private LocalDateTime createdAt;

    private String orderId;

    public static OrderResponse fromEntity(OrderEntity orderEntity) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.productId = orderEntity.getProductId();
        orderResponse.qty = orderEntity.getQty();
        orderResponse.unitPrice = orderEntity.getUnitPrice();
        orderResponse.totalPrice = orderEntity.getTotalPrice();
        orderResponse.createdAt = orderEntity.getCreatedAt();
        orderResponse.orderId = orderEntity.getOrderId();
        return orderResponse;
    }
}