
package com.mock.exchange.trade_service.model;
import lombok.AllArgsConstructor;
import lombok.*;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeDTO {
 private String symbol;
    private double price;
    private int quantity;
    private String buyOrderId;
    private String sellOrderId;
    private LocalDateTime timestamp;
}
