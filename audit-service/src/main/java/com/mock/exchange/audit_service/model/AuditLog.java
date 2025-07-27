package com.mock.exchange.audit_service.model;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * AuditLog represents a log record stored in MongoDB.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "audit_logs")
public class AuditLog {
   @Id
    private String id;

    private String service;   // e.g. auth-service, order-service

    private String type;      // e.g. LOGIN, ORDER_PLACED, TRADE_MATCHED

    private String userId;    // optional

    private String message;   // free-form message

    private LocalDateTime timestamp;
}
