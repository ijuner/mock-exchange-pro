package com.mock.exchange.order_service.dto;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEventDTO {
 private Long id;
    private String symbol;
    private String side; // BUY / SELL
    private double price;
    private int quantity;
    private LocalDateTime timestamp;
}
