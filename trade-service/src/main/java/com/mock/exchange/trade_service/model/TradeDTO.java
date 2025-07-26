package com.mock.exchange.trade_service.model;
import lombok.*;

import java.time.LocalDateTime;

/**
 * TradeDTO represents a trade message received from Kafka.
 * This must match the structure sent by match-engine-service.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeDTO {
  private String symbol;
    private double price;
    private int quantity;
    private String buyOrderId;
    private String sellOrderId;
    private LocalDateTime timestamp;
}
