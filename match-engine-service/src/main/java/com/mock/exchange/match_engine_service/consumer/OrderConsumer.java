package com.mock.exchange.match_engine_service.consumer;
import com.mock.exchange.match_engine_service.engine.MatchingEngine;
import com.mock.exchange.match_engine_service.model.OrderDTO;
import com.mock.exchange.match_engine_service.model.TradeDTO;
import com.mock.exchange.match_engine_service.service.IdempotencyService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
/**
 * OrderConsumer listens to the Kafka topic "order-events".
 * It receives OrderDTO messages and passes them to the matching engine.
 */
@Slf4j
// match-engine-service: consume orders, match, publish trades, persist trades idempotently
@Component
public class OrderConsumer {
       @Autowired private MatchingEngine engine;
    @Autowired private TradeRepository tradeRepo;
    @Autowired private IdempotencyService idempotency; // Redis-based
    @Autowired private KafkaTemplate<String, String> kafkaTemplate;
    /**
     * Consume order messages from Kafka:
     * - Manual ack to control offset committing
     * - For each order, run matching engine (could be multi-threaded internally)
     * - For each produced trade: ensure idempotency before saving to Mongo and publishing downstream
     */
    @KafkaListener(topics = "order-events", groupId = "match-engine-group")
    public void consumeOrderEvent(OrderDTO orderDTO) {
        log.info("Received order event: {}", orderDTO);
        
        // TODO: Implement matching engine logic here
         // TODO: pass this to MatchingEngine queue
        engine.enqueue(orderDTO);
    }

     @KafkaListener(topics = "order-events", containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            OrderDTO order = fromJson(record.value(), OrderDTO.class);

            // 1) Execute matching (internally may use per-symbol queue to keep order)
            List<TradeDTO> trades = engine.match(order);

            for (TradeDTO t : trades) {
                // 2) Idempotency check: skip if already processed (using event/trade id)
                boolean fresh = idempotency.markIfNew(t.tradeId(), Duration.ofHours(6));
                if (!fresh) continue;

                // 3) Persist trade in Mongo; unique index also protects from duplicates
                try {
                    tradeRepo.save(toEntity(t));
                } catch (DuplicateKeyException dup) {
                    // Duplicate due to race/ retry – safe to ignore
                }

                // 4) Publish to downstream topic with symbol as key (keep per-symbol ordering)
                kafkaTemplate.send("trade-events", t.symbol(), toJson(t));
            }

            // 5) Commit offset ONLY after all logic succeeded
            ack.acknowledge();

        } catch (Exception e) {
            // No ack => message will be re-delivered (retry). Add DLQ later if needed.
            // Log the error with enough context
            // logger.error("Failed to process record: {}", record, e);
        }
    }
}
