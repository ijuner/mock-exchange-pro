
package com.mock.exchange.order_service.entity;
import jakarta.persistence.*;
import lombok.*;
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
    private String symbol;
    private int quantity;
    private double price;

    private String status; // e.g. PENDING, FILLED, CANCELLED
}
