package gerenciaconfigsrc.models.dto;

import lombok.Data;

@Data
public class PasswordDto {
    private String password;
    private String confirmPassword;
    private String token;

    public PasswordDto() {
    }

    public PasswordDto(String password, String confirmPassword, String token) {
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.token = token;
    }

}
