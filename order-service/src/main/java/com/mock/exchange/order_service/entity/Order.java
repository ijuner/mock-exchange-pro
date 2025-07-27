
package com.mock.exchange.order_service.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String side; // BUY or SELL
    private String type; // e.g. LIMIT, MARKET
    private String symbol;
    private int quantity;
    private double price;
    private LocalDateTime timestamp; // Unix timestamp in milliseconds

    private String status; // e.g. PENDING, FILLED, CANCELLED
}
