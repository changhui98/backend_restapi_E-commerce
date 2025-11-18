package com.example.userservice.infrastructure.client;

import com.example.userservice.infrastructure.exception.FeignErrorDecoder;
import com.example.userservice.presentation.dto.OrderResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", configuration = FeignErrorDecoder.class)
public interface OrderServiceClient {

    @GetMapping("/order-service/{userId}/orders_ng")
    List<OrderResponse> getOrders(@PathVariable String userId);

}
