package com.example.orderservice.infrastructure.kafka;

import com.example.orderservice.presentation.dto.request.OrderRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderRequest send(String topic, OrderRequest orderDto) {

        // ObjectMapper는 Java 객체를 JSON 문자열로 변환하거나,
        // JSON 문자열을 Java 객체로 변환하는 Jackson의 핵심 클래스입니다.
        // Producer는 byte[] 또는 String으로만 Kafka에 전송할 수 있으므로
        // OrderRequest 객체 -> Json 문자열로 변환이 필요하다.
        ObjectMapper mapper = new ObjectMapper();

        // 변환된 Json 문자열을 저장할 변수
        String jsonInString = "";


        try {
            // writeValueAsString()
            // - orderDto(자바 객체)를 JSON 문자열로 직렬화(JSON String)합니다.
            // - KafkaProducer는 객체를 보낼 수 없고 오직 byte[]만 전송 가능하므로
            // 직렬화 과정은 반드시 필요합니다.
            jsonInString = mapper.writeValueAsString(orderDto);
        } catch (JsonProcessingException e) {

            // JSON 직렬화 과정에서 문제가 발생할 경우 예외 발생.
            // 예를 들어 DTO 내부에 순환 참조가 있거나, 직렬화 불가능한 타입이 포함된 경우.
            e.printStackTrace();
        }

        // KafkaTemplate을 이용해 메시지를 Kafka로 전송합니다.
        // send(topic, data)
        // - topic : Kafka 클러스터 내 목적지
        // - data : 앞서 JSON으로 직렬화한 문자열(Producer 내부에서 StringSerializer가 byte[]로 변환함)
        kafkaTemplate.send(topic, jsonInString);
        log.info("kafka Producer sent data from Order microservice: {}", orderDto);

        return orderDto;
    }
}
