package esvar.ua.dekanatcontrolpanel.repo;

import esvar.ua.dekanatcontrolpanel.entity.ErrorLog;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {

    Page<ErrorLog> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("""
        SELECT e FROM ErrorLog e
        WHERE (:env IS NULL OR e.env = :env)
          AND (:severity IS NULL OR e.severity = :severity)
          AND (:from IS NULL OR e.createdAt >= :from)
          AND (:to IS NULL OR e.createdAt <= :to)
    """)
    Page<ErrorLog> findByFilters(
        @Param("env") String env,
        @Param("severity") ErrorLog.Severity severity,
        @Param("from") Instant from,
        @Param("to") Instant to,
        Pageable pageable
    );
}
