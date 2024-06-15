package gerenciaconfigsrc.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import gerenciaconfigsrc.enums.RolesEnum;
import gerenciaconfigsrc.models.RequestEntity.UserRequest;
import gerenciaconfigsrc.models.User;
import gerenciaconfigsrc.models.dto.PasswordDto;
import gerenciaconfigsrc.models.dto.UserDto;
import javassist.NotFoundException;

public interface UserService {
    UserDto create(UserRequest user) throws NotFoundException;
    Optional<User> findByEmail(String email);
    UserDto save(User user);
    UserDto editUser(UserRequest userRequest) throws NotFoundException;
    UserDto deleteUser(String email);
    UserDto deleteLoggedUser();
    User getUserByPrincipal();
    boolean hasRole(User user, RolesEnum admin);
    Page<User> listUsersByPage(Pageable page);

    Page<User> listUsersByPageAndName(Pageable pages, String name);

    User getUserById(Long userId) throws NotFoundException;

    UserDto commonCreate(UserRequest request)throws NotFoundException;

    Optional<User> verifyUser(String email);

    String resetPassword(PasswordDto userPassword) throws IllegalArgumentException;

    String getIMC(Long userId);

    String getPesoIdeal(Long userId);

    String getCpfOfuscado(Long userId);

    String getSituacaoImc(Long userId);

    String getCpfValido(Long userId);

    String getCalculaIdade(Long userId);
}
