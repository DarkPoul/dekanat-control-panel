package esvar.ua.dekanatcontrolpanel.service;

import esvar.ua.dekanatcontrolpanel.entity.User;
import esvar.ua.dekanatcontrolpanel.repo.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class DatabaseUserDetailsService {

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByEmail(username)
                    .filter(User::isActive)
                    .orElseThrow(() -> new UsernameNotFoundException("User '%s' not found".formatted(username)));

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPasswordHash())
                    .roles(user.getRole().name())
                    .build();
        };
    }
}