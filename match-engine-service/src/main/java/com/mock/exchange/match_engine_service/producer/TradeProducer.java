package com.mock.exchange.match_engine_service.producer;
import com.mock.exchange.match_engine_service.model.TradeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * TradeProducer publishes matched trade records to Kafka.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TradeProducer {
 private final KafkaTemplate<String, TradeDTO> kafkaTemplate;

    private final String topic = "trade-events";

    /**
     * Sends a trade message to Kafka topic.
     * @param trade the matched trade record
     */
    public void sendTrade(TradeDTO trade) {
        kafkaTemplate.send(topic, trade);
        log.info("📤 Sent trade event to Kafka: {}", trade);
    }
}
