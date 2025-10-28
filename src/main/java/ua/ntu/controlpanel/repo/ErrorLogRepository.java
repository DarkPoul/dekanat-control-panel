package ua.ntu.controlpanel.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.ntu.controlpanel.entity.ErrorLog;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {
}
