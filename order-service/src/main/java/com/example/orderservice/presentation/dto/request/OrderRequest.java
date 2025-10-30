package com.example.orderservice.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private String productId;
    private Integer qty;
    private Integer unitPrice;

}