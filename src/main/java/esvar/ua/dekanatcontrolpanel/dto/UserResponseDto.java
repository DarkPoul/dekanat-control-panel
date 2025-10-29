package esvar.ua.dekanatcontrolpanel.dto;

import java.time.Instant;

public class UserResponseDto {

    private Long id;
    private String email;
    private String role;
    private boolean active;
    private Instant createdAt;

    public UserResponseDto() {
    }

    public UserResponseDto(Long id, String email, String role, boolean active, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.active = active;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
