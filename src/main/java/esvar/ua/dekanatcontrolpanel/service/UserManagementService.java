package esvar.ua.dekanatcontrolpanel.service;

import esvar.ua.dekanatcontrolpanel.dto.UserCreateUpdateDto;
import esvar.ua.dekanatcontrolpanel.dto.UserResponseDto;
import esvar.ua.dekanatcontrolpanel.entity.User;
import esvar.ua.dekanatcontrolpanel.repo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class UserManagementService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserManagementService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> listUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public UserResponseDto createUser(UserCreateUpdateDto dto) {
        if (!StringUtils.hasText(dto.getPassword())) {
            throw new IllegalArgumentException("Password must not be blank");
        }

        String email = StringUtils.trimWhitespace(dto.getEmail());

        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            throw new IllegalStateException("Email is already in use");
        }

        User user = new User();
        user.setEmail(email);
        user.setRole(parseRole(dto.getRole()));
        user.setActive(Boolean.TRUE.equals(dto.getActive()));
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setCreatedAt(Instant.now());

        User saved = userRepository.save(user);
        return toDto(saved);
    }

    public UserResponseDto updateUser(Long id, UserCreateUpdateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));

        user.setRole(parseRole(dto.getRole()));
        if (dto.getActive() != null) {
            user.setActive(dto.getActive());
        }

        return toDto(user);
    }

    public void changePassword(Long id, String newPassword) {
        if (!StringUtils.hasText(newPassword)) {
            throw new IllegalArgumentException("Password must not be blank");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
    }

    public UserResponseDto toggleActive(Long id, boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
        user.setActive(active);
        return toDto(user);
    }

    private UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getRole().name(),
                user.isActive(),
                user.getCreatedAt());
    }

    private User.Role parseRole(String value) {
        try {
            User.Role role = User.Role.valueOf(value);
            if (role == User.Role.DEAD) {
                throw new IllegalArgumentException("Role DEAD is not allowed");
            }
            return role;
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unknown role: " + value, ex);
        }
    }
}