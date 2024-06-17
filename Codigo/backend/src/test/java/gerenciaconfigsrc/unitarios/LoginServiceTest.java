package gerenciaconfigsrc.unitarios;

import gerenciaconfigsrc.aceitacao.steps.TestLogin;
import gerenciaconfigsrc.configs.jwt.JwtTokenUtil;
import gerenciaconfigsrc.enums.RolesEnum;
import gerenciaconfigsrc.enums.SexEnum;
import gerenciaconfigsrc.models.*;
import gerenciaconfigsrc.models.RequestEntity.LoginRequest;
import gerenciaconfigsrc.models.ResponseEntity.LoginResponse;
import gerenciaconfigsrc.repository.UserRepository;
import gerenciaconfigsrc.service.AuthService;
import gerenciaconfigsrc.service.UserRoleService;
import gerenciaconfigsrc.service.UserService;
import gerenciaconfigsrc.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
public class LoginServiceTest {

    @InjectMocks
    private AuthServiceImpl loginService;

    @Autowired
    private PasswordEncoder bcryptEncoder;
    @Mock
    private UserService userService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    UserRepository userRepository;
    @Mock
    UserRoleService userRole;

    @Autowired
    ApplicationContext context;

    @Test
    public void testLogin() throws Exception {

        Cities city = new Cities("BH");
        States states = new States("BR","MG");

        Address address = new Address(
                "Rua",
                123,
                "bairro",
                city,
                states,
                "30690740"
        );
        List<Role> roles = new LinkedList<>();
        roles.add(new Role(RolesEnum.ADMIN.getCode()));


        User userToLogin = new User(
                Long.getLong("30"),
                "testeUnitarioLogin",
                "testeunitario@hotmail.com",
                this.bcryptEncoder.encode("12345678"),
                SexEnum.MALE.getCode(),
                "12345678951",
                address,
                roles,
                new Date(),
                "33853056",
                "33853056",
                Integer.getInteger("32"),
                1.70,
                104.0,
                "latNameTest",
                25.0,
                null,
                new Date(),
                null
        );

        Optional<User> optionalUser=  Optional.of(userToLogin);
        Role role = new Role(RolesEnum.ADMIN.getCode());

        List<UserRole> userRole1 = new LinkedList<UserRole>();
        userRole1.add(new UserRole(userToLogin,role,new Date(),null) );

        Mockito.when(userRepository.findOneByEmailAndDeletedAtIsNull(any())).thenReturn(userToLogin);
        Mockito.when(userService.findByEmail(any())).thenReturn(optionalUser);
        Mockito.when(userRole.findAllByUser(any())).thenReturn(userRole1);
        //Mockito.when(loginService.authenticate(any(),any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        LoginRequest login = new LoginRequest(userToLogin.getEmail(),userToLogin.getPassword());

        LoginResponse response =  loginService.login(login);


        Assertions.assertEquals(response.getUser().getEmail(),("testeunitario@hotmail.com"));

    }

}