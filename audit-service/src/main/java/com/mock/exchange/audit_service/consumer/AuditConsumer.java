
package com.mock.exchange.audit_service.consumer;
import com.mock.exchange.audit_service.model.AuditLog;
import com.mock.exchange.audit_service.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka listener that consumes audit-events and stores in MongoDB.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuditConsumer {
 private final AuditLogRepository auditLogRepository;

    @KafkaListener(topics = "audit-events", groupId = "audit-consumer")
    public void listen(AuditLog logEntry) {
        log.info("📥 Received audit log: {}", logEntry);
        auditLogRepository.save(logEntry);
        log.info("💾 Stored to MongoDB.");
    }
}
