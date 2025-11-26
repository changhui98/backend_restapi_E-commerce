package com.example.catalogservice.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessage {

    private String productId;
    private Integer qty;
    private Integer unitPrice;

}
