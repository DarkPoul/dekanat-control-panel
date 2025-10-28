package esvar.ua.dekanatcontrolpanel.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import esvar.ua.dekanatcontrolpanel.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
