package gerenciaconfigsrc.unitarios;

import gerenciaconfigsrc.configs.jwt.JwtTokenUtil;
import gerenciaconfigsrc.enums.RolesEnum;
import gerenciaconfigsrc.enums.SexEnum;
import gerenciaconfigsrc.models.*;
import gerenciaconfigsrc.models.RequestEntity.UserRequest;
import gerenciaconfigsrc.models.dto.AddressDto;
import gerenciaconfigsrc.repository.AddressRepository;
import gerenciaconfigsrc.repository.PasswordResetServiceRepository;
import gerenciaconfigsrc.repository.UserRepository;
import gerenciaconfigsrc.service.*;
import gerenciaconfigsrc.service.impl.AuthServiceImpl;
import gerenciaconfigsrc.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import gerenciaconfigsrc.enums.RolesEnum;
import gerenciaconfigsrc.models.*;
import gerenciaconfigsrc.models.dto.*;
import gerenciaconfigsrc.repository.*;
import gerenciaconfigsrc.service.*;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.persistence.NonUniqueResultException;
import java.util.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
public class UserServiceTest {


    @Mock
    private AuthServiceImpl loginService;

    @Mock
    private PasswordEncoder bcryptEncoder;
    @InjectMocks
    private UserServiceImpl userService;

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

    @Mock
    private RoleService roleService;

    @Mock
    private CityService cityService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private StateService stateService;

    @Mock
    private PasswordResetServiceRepository passwordResetServiceRepository;


    @Test
    void testCreateUser() throws NotFoundException {

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
        AddressDto addressDto = new AddressDto("Rua",
                123,
                "bairro",
                "BH",
                "MG",
                "30690740");

        List<Role> roles = new LinkedList<>();
        roles.add(new Role(RolesEnum.ADMIN.getCode()));

        List<String> rolesString = new LinkedList<>();
        rolesString.add("ADMIN");

        Date actualDate = new Date();

        UserRequest userToCreate = new UserRequest("testeUnitarioLogin","testeunitario@hotmail.com", "12345678", rolesString,"12345678951",
                addressDto, "M", actualDate, "33853056", "33853056",
               30L, "32", "1.70", "104.0", "latNameTest", "25.0");

        User createdUser = new User(
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

        //given

        when(roleService.findAllByNameIn(anyList())).thenReturn(Collections.singletonList(new Role("USER")));
        when(bcryptEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(cityService.findByCity(anyString())).thenReturn(city);
        when(stateService.findByUf(anyString())).thenReturn(states);
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(userRepository.save(any(User.class))).thenReturn(createdUser);
        when(userRepository.findOneByEmailAndDeletedAtIsNull(anyString())).thenReturn(null);


        //when
        UserDto userDto = userService.create(userToCreate);

        // Then
        assertNotNull(userDto);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_EmailAlreadyExists() {
        AddressDto addressDto = new AddressDto("Rua",
                123,
                "bairro",
                "BH",
                "MG",
                "30690740");

        List<Role> roles = new LinkedList<>();
        roles.add(new Role(RolesEnum.ADMIN.getCode()));

        List<String> rolesString = new LinkedList<>();
        rolesString.add("ADMIN");

        Date actualDate = new Date();

        UserRequest userToCreate = new UserRequest("testeUnitarioLogin","testeunitario@hotmail.com", "12345678", rolesString,"12345678951",
                addressDto, "M", actualDate, "33853056", "33853056",
                30L, "32", "1.70", "104.0", "latNameTest", "25.0");

        when(userRepository.findOneByEmailAndDeletedAtIsNull(anyString())).thenReturn(new User());

        assertThrows(NonUniqueResultException.class, () -> {
            userService.create(userToCreate);
        });
    }

    @Test
    void testVerifyUser() {
        when(userRepository.findOneByEmailAndDeletedAtIsNull(anyString())).thenReturn(new User());

        Optional<User> user = userService.verifyUser("test@example.com");

        assertTrue(user.isPresent());
    }

    @Test
    void testResetPassword_TokenExpired() {
        PasswordDto passwordDto = new PasswordDto();
        passwordDto.setPassword("newPassword");
        passwordDto.setConfirmPassword("newPassword");
        passwordDto.setToken("expiredToken");

        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setExpirieAt(new Date(System.currentTimeMillis() - 10000)); // expired token

        when(passwordResetServiceRepository.findByTokenAndDeletedAtIsNull(anyString())).thenReturn(passwordReset);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.resetPassword(passwordDto);
        });
    }

    @Test
    void testGetIMC() {
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

        User createdUser = new User(
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
        createdUser.setLength(1.75);  // Altura em metros
        createdUser.setWeight(70.0);  // Peso em kg

        when(userRepository.findOneByEmailAndDeletedAtIsNull(anyString())).thenReturn(createdUser);
        when(userRepository.findOneByIdUser(anyLong())).thenReturn(createdUser);


        Double imc = Double.valueOf(userService.getIMC(30L));

        assertNotNull(imc);
        assertEquals(22.86, imc, 0.01);  // Comparando com uma margem de erro
    }

    @Test
    void testGetPesoIdeal_Masculino() {
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

        User createdUser = new User(
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
        createdUser.setIdUser(30L);
        createdUser.setLength(1.75);
        createdUser.setWeight(70.0);
        createdUser.setSex("M");

        when(userRepository.findOneByIdUser(anyLong())).thenReturn(createdUser);

        Double pesoIdeal = Double.valueOf(userService.getPesoIdeal(30L));
        System.out.println("resultado Masculino: "+pesoIdeal);

        assertEquals(69.225, pesoIdeal, 0.1);
    }

    @Test
    void testGetPesoIdeal_Feminino() {
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

        User createdUser = new User(
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


        createdUser.setIdUser(30L);
        createdUser.setLength(1.75);
        createdUser.setWeight(60.0);
        createdUser.setSex("F");

        when(userRepository.findOneByIdUser(anyLong())).thenReturn(createdUser);

        Double pesoIdeal = Double.valueOf(userService.getPesoIdeal(1L));
        System.out.println("resultado Feminino: "+pesoIdeal);
        assertEquals(63.975, pesoIdeal, 0.1);
    }

    @Test
    void testGetCpfOfuscado() {
        User user = new User();
        user.setIdUser(30L);
        user.setLegalDocument("12345678901");

        when(userRepository.findOneByIdUser(anyLong())).thenReturn(user);

        String cpfOfuscado = userService.getCpfOfuscado(30L);

        assertEquals("***456*****", cpfOfuscado);
    }

    @Test
    void testGetSituacaoImc() {
        User user = new User();
        user.setIdUser(1L);
        user.setWeight(70.0);
        user.setLength(1.75); // altura em cm

        when(userRepository.findOneByIdUser(anyLong())).thenReturn(user);

        String situacaoImc = userService.getSituacaoImc(1L);

        assertEquals("Peso normal", situacaoImc);
    }

    @Test
    void testGetCpfValido_Valido() {
        User user = new User();
        user.setIdUser(1L);
        user.setLegalDocument("12345678900");

        when(userRepository.findOneByIdUser(anyLong())).thenReturn(user);

        String cpfValido = userService.getCpfValido(1L);

        assertEquals("true", cpfValido);
    }

    @Test
    void testGetCpfValido_Invalido() {
        User user = new User();
        user.setIdUser(1L);
        user.setLegalDocument("12345678901");

        when(userRepository.findOneByIdUser(anyLong())).thenReturn(user);

        String cpfValido = userService.getCpfValido(1L);

        assertEquals("false", cpfValido);
    }

    @Test
    void testGetCalculaIdade() {
        User user = new User();
        user.setIdUser(1L);
        Calendar calendar = Calendar.getInstance();
        calendar.set(1990, Calendar.JANUARY, 1);
        user.setBirthDate(calendar.getTime());

        when(userRepository.findOneByIdUser(anyLong())).thenReturn(user);

        String idade = userService.getCalculaIdade(1L);

        assertEquals("34", idade);
    }

    @Test
    void testCalcularIMC() {
        Double peso = 70.0;
        Double altura = 1.75;

        Double imc = userService.calcularIMC(peso, altura);

        assertEquals(22.86, imc, 0.01); // Verifica com uma margem de erro
    }
}


