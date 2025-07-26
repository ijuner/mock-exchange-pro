package com.mock.exchange.audit_service.repository;
import com.mock.exchange.auditservice.model.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
public class AuditLogRepository interface AuditLogRepository extends MongoRepository<AuditLog, String> {

}
