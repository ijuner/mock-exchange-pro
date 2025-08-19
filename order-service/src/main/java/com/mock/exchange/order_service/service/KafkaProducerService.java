package com.mock.exchange.order_service.service;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.mock.exchange.order_service.dto.OrderEventDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publishOrderEvent(OrderEventDTO order) {
        /**
 * Publish an order to Kafka with key = symbol.
 * Same key => same partition => per-symbol ordering guaranteed.
 */
    String topic = "order-events";
    String key = order.getSymbol(); // partition key
    String value = toJson(order);
    kafkaTemplate.send(topic, key, value);
    }

    private String toJson(OrderEventDTO order) {
        try {
            return new ObjectMapper().writeValueAsString(order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert OrderEventDTO to JSON", e);
        }
    }
}
