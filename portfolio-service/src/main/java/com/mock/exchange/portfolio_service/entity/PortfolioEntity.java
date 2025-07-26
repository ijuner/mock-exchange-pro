package com.mock.exchange.portfolio_service.entity;
import jakarta.persistence.*;
import lombok.*;

/**
 * PortfolioEntity represents user holdings.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioEntity {
 @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String symbol;

    private int quantity;
}
