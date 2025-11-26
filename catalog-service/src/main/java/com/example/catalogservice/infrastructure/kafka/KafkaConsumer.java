package com.example.catalogservice.infrastructure.kafka;

import com.example.catalogservice.domain.entity.CatalogEntity;
import com.example.catalogservice.infrastructure.repository.CatalogJpaRepository;
import com.example.catalogservice.presentation.dto.response.OrderMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final CatalogJpaRepository catalogJpaRepository;

    @KafkaListener(topics = "example-catalog-topic")
    // Consumer 가 Kafka 메시지를 자동으로 구독하고 실행 하도록 만드는 어노테이션.
    // 이 어노테이션이 있어야 spring 에서 지정한 topic을 계속 구독하고, 새로운 메시지가 들어오면 해당 메서드를 자동으로 호출해준다.
    // Producer 에서는 send()를 직접 호출해야하지만,
    // Consumer는 @KafkaListener만 붙이면 자동 실행 되는 구조이다.
    public void updateQty(String kafkaMessage) {
        log.info("Kafka Message : {}", kafkaMessage);

        // Kafka Message는 JSON 이기 때문에, JSON -> Map 형태로 파싱하여 담을 변수 선언.
//        Map<Object, Object> map = new HashMap<>();

        // Producer가 보낸 데이터가 DTO구조이면 그대로 DTO로 받는 것이 훨~~~씬 안전함.
        // 공식문서에서는 절대로 귀찮다고 엔티티로 받으면 절대 절대 안된다고 함.
        // DTO를 사용하면 타입 안전성이 있고, 잘못된 필드명 -> 컴파일 에러로 보여준다.
        // 유지보수 쉽고 구조가 명확함.
        OrderMessage orderMessage = null;

        // Jackson의 ObjectMapper (JSON문자열을 -> Java 객체 변환)
        ObjectMapper mapper = new ObjectMapper();

        try {

            // readValue :
            // kafkaMessage(Json 문자열)를 Map<Object, Object>구조로 역직렬화
            // Producer가 보낸 JSON을 DTO로 변환해도 되지만, 여기서는 Map으로 단순 파싱함
//            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
//                // Map<Object, Object>
//                // List<String>
//                // List<OrderRequest>
//                // 이런 제네릭 타입을 Jackson이 역직렬화할 때, JVM의 타입 소거 때문에 실제 타입 정보를 잃어버린다.
//                // 그래서 Jackson 에 타입 정보를 알려주기 위해 TypeReference<>를 사용한다.
//            });
            orderMessage = mapper.readValue(kafkaMessage, OrderMessage.class);

        } catch (JsonProcessingException e) {

            // JSON 파싱 실패 시 예외
            e.printStackTrace();
        }

        // productId로 CatalogEntity 조회
        // map.get("productId")가 Object 타입이므로 (String) 으로 캐스팅 필요
//        CatalogEntity entity = catalogJpaRepository.findByProductId((String)map.get("productId")).orElseThrow(
//            () -> new RuntimeException("product not found")
//        );

        CatalogEntity entity = catalogJpaRepository.findByProductId(
            orderMessage.getProductId()).orElseThrow(
            () -> new RuntimeException("product not found")
        );

        // 재고 (qty) 값을 꺼내서 update
        // map.get("qty")가 Object 이므로 (Integer) 캐스팅 필요
//        entity.updateStock((Integer)map.get("qty"));
        entity.updateStock(orderMessage.getQty());

        // 변경된 엔티티 저장 - DB 업데이트.
        catalogJpaRepository.save(entity);
    }
}
