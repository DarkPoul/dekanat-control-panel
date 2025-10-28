package esvar.ua.dekanatcontrolpanel.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import esvar.ua.dekanatcontrolpanel.entity.ErrorLog;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {
}
