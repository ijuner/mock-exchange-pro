package com.mock.exchange.match_engine_service.consumer;
import com.mock.exchange.match_engine_service.engine.MatchingEngine;
import com.mock.exchange.match_engine_service.model.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
/**
 * OrderConsumer listens to the Kafka topic "order-events".
 * It receives OrderDTO messages and passes them to the matching engine.
 */
@Slf4j
@Component
public class OrderConsumer {
    private final MatchingEngine matchingEngine = new MatchingEngine();
/**
 * 
     * This method is automatically called when a new message is published to "order-events".
     * The JSON message is automatically converted into OrderDTO.
     *
     * @param order the received OrderDTO object from Kafka
     */
    @KafkaListener(topics = "order-events", groupId = "match-engine-group")
    public void consumeOrderEvent(OrderDTO orderDTO) {
        log.info("Received order event: {}", orderDTO);
        
        // TODO: Implement matching engine logic here
         // TODO: pass this to MatchingEngine queue
        matchingEngine.enqueue(orderDTO);
    }
}
