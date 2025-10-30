package com.example.catalogservice.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogRequest {

    private String productId;
    private String productName;
    private Integer stock;
    private Integer unitPrice;

}