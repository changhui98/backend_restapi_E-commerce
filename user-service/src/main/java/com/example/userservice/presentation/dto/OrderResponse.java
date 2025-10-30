package com.example.userservice.presentation.dto;

import java.util.Date;
import lombok.Data;

@Data
public class OrderResponse {

    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private Date createAt;

    private String orderId;
}