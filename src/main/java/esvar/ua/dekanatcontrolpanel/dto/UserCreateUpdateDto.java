package esvar.ua.dekanatcontrolpanel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UserCreateUpdateDto {

    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^(ADMIN|TEST_MANAGER|VIEWER)$")
    private String role;

    @NotNull
    private Boolean active;

    private String password;

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
