package esvar.ua.dekanatcontrolpanel.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import esvar.ua.dekanatcontrolpanel.entity.AuditEvent;

public interface AuditEventRepository extends JpaRepository<AuditEvent, Long> {
}
