package gerenciaconfigsrc.models.dto;

import gerenciaconfigsrc.models.User;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private List <RoleDto> roles;

    public UserDto() {
    }

    public UserDto(Long id, String name, String email, List<RoleDto> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    public static UserDto fromUser(User user){
        List<RoleDto> roles = user.getRoles()
                .stream()
                .map(RoleDto::toRole)
                .collect(Collectors.toList());

        return new UserDto(user.getIdUser(), user.getName(), user.getEmail(), roles);
    }

}
