package com.mock.exchange.order_service.service;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic = "order-events";
    public void publishOrderEvent(String orderJson) {
        kafkaTemplate.send(topic, orderJson);
    }
}
