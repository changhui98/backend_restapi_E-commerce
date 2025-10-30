package com.example.catalogservice.presentation.dto.vo;

import lombok.Data;

@Data
public class CatalogDto {

    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;

    private String orderId;
    private String userId;

}