package gerenciaconfigsrc.service.impl;

import gerenciaconfigsrc.models.Role;
import gerenciaconfigsrc.models.User;
import gerenciaconfigsrc.models.UserRole;
import gerenciaconfigsrc.repository.UserRoleRepository;
import gerenciaconfigsrc.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleRepository roleRepository;

    @Override
    public List<Role> findAllByUserId(Long id) {
        return null;// this.roleRepository.findAllByUserId(id);
    }

    @Override
    public List<UserRole> findAllByUser(User user) {
        return this.roleRepository.findAllByUser(user);
    }


}
