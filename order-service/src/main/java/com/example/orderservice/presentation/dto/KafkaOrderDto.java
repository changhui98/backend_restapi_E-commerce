package com.example.orderservice.presentation.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaOrderDto implements Serializable {

    private Schema schema;
    private Payload payload;

}
