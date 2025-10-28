package ua.ntu.controlpanel.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.ntu.controlpanel.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
