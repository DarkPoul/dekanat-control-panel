package esvar.ua.dekanatcontrolpanel.dto;

import jakarta.validation.constraints.NotBlank;

public class UserPasswordChangeDto {

    @NotBlank
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
