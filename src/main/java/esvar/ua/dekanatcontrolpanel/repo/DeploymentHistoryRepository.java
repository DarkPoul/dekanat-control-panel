package esvar.ua.dekanatcontrolpanel.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import esvar.ua.dekanatcontrolpanel.entity.DeploymentHistory;

public interface DeploymentHistoryRepository extends JpaRepository<DeploymentHistory, Long> {
}
