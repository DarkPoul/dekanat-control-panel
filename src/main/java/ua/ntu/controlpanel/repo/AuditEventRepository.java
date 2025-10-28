package ua.ntu.controlpanel.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.ntu.controlpanel.entity.AuditEvent;

public interface AuditEventRepository extends JpaRepository<AuditEvent, Long> {
}
