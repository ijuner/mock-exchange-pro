package com.mock.exchange.portfolio_service.consumer;
import com.mock.exchange.portfolio_service.entity.PortfolioEntity;
import com.mock.exchange.portfolio_service.model.TradeDTO;
import com.mock.exchange.portfolio_service.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Kafka listener that updates holdings on trade events.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PortfolioConsumer {
private final PortfolioRepository repository;

    @KafkaListener(topics = "trade-events", groupId = "portfolio-consumer")
    public void listen(TradeDTO dto) {
        log.info("📥 Received trade: {}", dto);

        // 买方持仓增加
        updatePosition(dto.getBuyOrderId(), dto.getSymbol(), dto.getQuantity());

        // 卖方持仓减少
        updatePosition(dto.getSellOrderId(), dto.getSymbol(), -dto.getQuantity());
    }

    private void updatePosition(String userId, String symbol, int delta) {
        Optional<PortfolioEntity> optional = repository.findByUserIdAndSymbol(userId, symbol);

        if (optional.isPresent()) {
            PortfolioEntity p = optional.get();
            p.setQuantity(p.getQuantity() + delta);
            repository.save(p);
        } else {
            PortfolioEntity newPortfolio = PortfolioEntity.builder()
                    .userId(userId)
                    .symbol(symbol)
                    .quantity(delta)
                    .build();
            repository.save(newPortfolio);
        }

        log.info("📊 Updated portfolio for {}: {} {}", userId, symbol, delta);
    }
}
