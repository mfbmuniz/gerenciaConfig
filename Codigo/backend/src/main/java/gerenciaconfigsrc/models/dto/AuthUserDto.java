package gerenciaconfigsrc.models.dto;

import gerenciaconfigsrc.enums.RolesEnum;
import gerenciaconfigsrc.models.User;
import lombok.Data;

import java.util.List;

@Data
public class AuthUserDto {
    User user;
    List<RolesEnum> roles;

    public AuthUserDto(User user, List<RolesEnum> roles) {
        this.user = user;
        this.roles = roles;
    }
}
