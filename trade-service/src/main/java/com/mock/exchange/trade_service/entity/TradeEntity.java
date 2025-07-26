package com.mock.exchange.trade_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * TradeEntity is the database representation of a trade.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "trades")
public class TradeEntity {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    private double price;

    private int quantity;

    private String buyOrderId;
    private String sellOrderId;

    private LocalDateTime timestamp;
}
