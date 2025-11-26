package com.example.orderservice.presentation;

import com.example.orderservice.application.service.OrderService;
import com.example.orderservice.infrastructure.kafka.KafkaProducer;
import com.example.orderservice.infrastructure.kafka.OrderProducer;
import com.example.orderservice.presentation.dto.request.OrderRequest;
import com.example.orderservice.presentation.dto.response.OrderResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-service")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;

    @PostMapping("/{userId}/orders")
    public ResponseEntity<?> createOrder(
        @PathVariable("userId") String userId,
        @RequestBody OrderRequest orderRequest
    ) {
        OrderResponse orderResponse = orderService.createOrder(userId, orderRequest);

        // 재고 차감 이벤트 발행
        kafkaProducer.send("example-catalog-topic", orderRequest);

        // 서비스 스케일 아웃 시 데이터 정합성
        orderProducer.send("orders", orderResponse, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderResponse>> orderList(
        @PathVariable("userId") String userId
    ) {

        List<OrderResponse> ordersByUserId = orderService.getOrdersByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(ordersByUserId);
    }



}
