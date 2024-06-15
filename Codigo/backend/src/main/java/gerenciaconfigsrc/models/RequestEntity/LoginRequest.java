package gerenciaconfigsrc.models.RequestEntity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class LoginRequest {
    @NotNull(message = "Campo email não pode ser nulo")
    @NotEmpty(message = "Campo email não pode ser vazio")
    private String email;
    @NotNull(message = "Campo password não pode ser nulo")
    @NotEmpty(message = "Campo password não pode ser vazio")
    private String password;

    public LoginRequest(){}

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

