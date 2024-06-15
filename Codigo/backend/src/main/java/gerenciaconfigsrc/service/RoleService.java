package gerenciaconfigsrc.service;

import gerenciaconfigsrc.models.Role;

import java.util.List;

public interface RoleService {

    Role findByName(String name);

    List<Role> findAllByNameIn(List<String> roles);
}
