package gerenciaconfigsrc.service;

import gerenciaconfigsrc.models.Role;
import gerenciaconfigsrc.models.User;
import gerenciaconfigsrc.models.UserRole;

import java.util.List;

public interface UserRoleService {


    List<Role> findAllByUserId(Long id);
    List<UserRole> findAllByUser(User id);
}
