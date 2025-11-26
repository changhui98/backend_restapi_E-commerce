package com.example.catalogservice.infrastructure.kafka;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumerGroupId");
        // Consumer가 어디까지 메시지를 읽었는지 저장하는 것이 offset이다.
        // Kafka는 offset을 _consumer_offsets 내부 토픽에 저장하는데
        // 저장 기준이 바로 GroupId이다.
        // 즉 groupId가 있어야 몇 번째까지 읽었다를 저장하고 이어서 읽을 수 있다.

        // 같은 그룹 내 다른 Consumer가 같은 메시지를 두 번 읽지 않도록 한다.

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {

        // @KafkaListener가 메시지를 병렬적으로 가져오기 위한 컨테이너 팩토리
        // Spring Kafka 공식 문서 기준 : ListenerContainer는 Consumer 스레드 풀을 관리함
        ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory
            = new ConcurrentKafkaListenerContainerFactory<>();

        // Consumer 생성 전략을 위에서 만든 consumerFactory로 지정
        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory());

        return kafkaListenerContainerFactory;
    }

}
