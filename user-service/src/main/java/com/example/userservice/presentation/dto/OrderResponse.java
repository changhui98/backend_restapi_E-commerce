package com.example.userservice.presentation.dto;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

@Data
public class OrderResponse {

    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private LocalDateTime createdAt;

    private String orderId;
}
