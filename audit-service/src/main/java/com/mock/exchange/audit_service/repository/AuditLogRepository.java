package com.mock.exchange.audit_service.repository;
import com.mock.exchange.audit_service.model.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface AuditLogRepository extends MongoRepository<AuditLog, String> {

}
