
package com.mock.exchange.trade_service.consumer;

import com.mock.exchange.trade_service.model.TradeDTO;
import com.mock.exchange.trade_service.entity.TradeEntity;
import com.mock.exchange.trade_service.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
        

/**
 * TradeConsumer listens to Kafka topic "trade-events"
 * and stores the trade to local database.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TradeConsumer {
private final TradeRepository tradeRepository;

    @KafkaListener(topics = "trade-events", groupId = "trade-consumer")
    public void consume(TradeDTO dto) {
        log.info("📥 Received trade from Kafka: {}", dto);

        TradeEntity trade = TradeEntity.builder()
                .symbol(dto.getSymbol())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .buyOrderId(dto.getBuyOrderId())
                .sellOrderId(dto.getSellOrderId())
                .timestamp(dto.getTimestamp())
                .build();

        tradeRepository.save(trade);
        log.info("💾 Saved trade to DB: {}", trade);
    }
}
