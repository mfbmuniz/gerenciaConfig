package gerenciaconfigsrc.models.ResponseEntity;

import gerenciaconfigsrc.models.dto.UserDto;
import lombok.Data;

import java.util.Set;

@Data
public class LoginResponse {

    private final String token;
    private final UserDto user;
    private final Set<String> roles;

    public LoginResponse(String token, UserDto user, Set<String> roles) {
        this.token = token;
        this.user = user;
        this.roles = roles;
    }

}
