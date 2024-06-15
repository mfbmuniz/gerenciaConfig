package gerenciaconfigsrc.models.RequestEntity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CheckExistsRequest {
    @NotNull(message = "Campo email não pode ser nulo")
    @NotEmpty(message = "Campo email não pode ser vazio")
    private String email;

    public String getEmail(){
        return this.email;
    }
}
