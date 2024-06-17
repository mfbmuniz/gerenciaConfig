package gerenciaconfigsrc.integracao;
import gerenciaconfigsrc.controller.PacientesController;
import com.fasterxml.jackson.databind.ObjectMapper;
import gerenciaconfigsrc.enums.RolesEnum;
import gerenciaconfigsrc.enums.SexEnum;
import gerenciaconfigsrc.models.*;
import gerenciaconfigsrc.models.RequestEntity.UserRequest;
import gerenciaconfigsrc.models.dto.AddressDto;
import gerenciaconfigsrc.repository.UserRepository;
import gerenciaconfigsrc.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {


    @ExtendWith(SpringExtension.class)
    @WebMvcTest(PacientesController.class)
    @SpringBootTest(
            webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
    )
    @AutoConfigureMockMvc
    public class UserServiceImplIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private PasswordEncoder bcryptEncoder;

        @Autowired
        private UserServiceImpl userService;

        @Autowired
        private UserRepository userRepository;


        private User userObject;
        private UserRequest userToCreate;

        @BeforeEach
        void setUp() {
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

            userToCreate = new UserRequest("testeUnitarioLogin","testeunitario@hotmail.com", "12345678", rolesString,"12345678951",
                    addressDto, "M", actualDate, "33853056", "33853056",
                    30L, "32", "1.70", "104.0", "latNameTest", "25.0");

            userObject = new User(
                    Long.getLong("300"),
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
        }

        @Test
        void testCriaUsuarioTeste() throws Exception {
            //given(userService.getPesoIdeal(anyLong())).willReturn("69.225");

            // Create JSON manually

            ObjectMapper mapper = new ObjectMapper();

            mapper.writeValueAsString(userToCreate);

            mockMvc.perform(MockMvcRequestBuilders.post("/pacientes/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.toString())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", equalTo("testeUnitarioLogin")))
                    .andExpect(jsonPath("$.email", equalTo("testeunitario@hotmail.com")));
        }

        // Perform the POST request and assertions


        @Test
        void testGetPesoIdeal() throws Exception {
            //given(userService.getPesoIdeal(anyLong())).willReturn("69.225");

            mockMvc.perform(MockMvcRequestBuilders.get("/users/300/pesoIdeal")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("69.225"));
        }

        @Test
        void testGetCpfOfuscado() throws Exception {
            given(userService.getCpfOfuscado(anyLong())).willReturn("12****789**");

            mockMvc.perform(MockMvcRequestBuilders.get("/users/1/cpfOfuscado")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("12****789**"));
        }

        @Test
        void testGetSituacaoImc() throws Exception {
            given(userService.getSituacaoImc(anyLong())).willReturn("Peso normal");

            mockMvc.perform(MockMvcRequestBuilders.get("/users/1/situacaoImc")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Peso normal"));
        }

        @Test
        void testGetCpfValido() throws Exception {
            given(userService.getCpfValido(anyLong())).willReturn("true");

            mockMvc.perform(MockMvcRequestBuilders.get("/users/1/cpfValido")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));
        }

        @Test
        void testGetCalculaIdade() throws Exception {
            given(userService.getCalculaIdade(anyLong())).willReturn("34");

            mockMvc.perform(MockMvcRequestBuilders.get("/users/1/calculaIdade")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("34"));
        }

        @Test
        void testCalcularIMC() throws Exception {
            given(userService.calcularIMC(70.0, 1.75)).willReturn(22.86);

            mockMvc.perform(MockMvcRequestBuilders.get("/users/calcularIMC")
                            .param("peso", "70.0")
                            .param("altura", "175")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("22.86"));
        }
    }
}
