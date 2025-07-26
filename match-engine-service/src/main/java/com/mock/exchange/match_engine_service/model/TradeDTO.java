package com.mock.exchange.match_engine_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * TradeDTO represents a matched order transaction.
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
