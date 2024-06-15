package gerenciaconfigsrc.models.dto;

import gerenciaconfigsrc.models.Role;
import lombok.Data;

@Data
public class RoleDto {
    private String name;

    public RoleDto() {
    }

    public RoleDto(String name) {
        this.name = name;
    }

    public static RoleDto toRole(Role role){
        return new RoleDto(role.getName());
    }
}
