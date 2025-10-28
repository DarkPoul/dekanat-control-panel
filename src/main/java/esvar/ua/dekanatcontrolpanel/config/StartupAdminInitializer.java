package esvar.ua.dekanatcontrolpanel.config;

import java.time.Instant;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import esvar.ua.dekanatcontrolpanel.entity.User;
import esvar.ua.dekanatcontrolpanel.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class StartupAdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StartupAdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setEmail("admin@ntu");
            admin.setPasswordHash(passwordEncoder.encode("admin"));
            admin.setRole(User.Role.ADMIN);
            admin.setActive(true);
            admin.setCreatedAt(Instant.now());
            userRepository.save(admin);
        }
    }
}
