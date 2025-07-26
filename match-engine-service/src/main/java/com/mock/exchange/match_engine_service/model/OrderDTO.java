package com.mock.exchange.match_engine_service.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * DTO (Data Transfer Object) used to deserialize order messages from Kafka.
 * This matches the structure of JSON sent by order-service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;

    private String symbol; // e.g. "AAPL"

    private String side;   // "BUY" or "SELL"

    private double price;

    private int quantity;

    private LocalDateTime timestamp;
}
