package ua.ntu.controlpanel.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.ntu.controlpanel.entity.DeploymentHistory;

public interface DeploymentHistoryRepository extends JpaRepository<DeploymentHistory, Long> {
}
