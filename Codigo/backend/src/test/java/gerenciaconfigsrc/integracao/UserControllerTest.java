package gerenciaconfigsrc.integracao;
import gerenciaconfigsrc.controller.PacientesController;
import com.fasterxml.jackson.databind.ObjectMapper;
import gerenciaconfigsrc.enums.RolesEnum;
import gerenciaconfigsrc.enums.SexEnum;
import gerenciaconfigsrc.models.*;
import gerenciaconfigsrc.models.RequestEntity.LoginRequest;
import gerenciaconfigsrc.models.RequestEntity.UserRequest;
import gerenciaconfigsrc.models.dto.AddressDto;
import gerenciaconfigsrc.repository.UserRepository;
import gerenciaconfigsrc.service.StateService;
import gerenciaconfigsrc.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
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
/*@ExtendWith(SpringExtension.class)
@WebMvcTest(PacientesController.class)*/
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
public class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private PasswordEncoder bcryptEncoder;

        @Autowired
        private UserServiceImpl userService;

        @Autowired
        UserRepository userRepository;

        private String jwtToken;
        private User userObject;

        private String emailTeste;
        private UserRequest userToCreate;

        @BeforeEach
        void setUp() throws Exception {
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

            emailTeste = "testeunitario13@hotmail.com";

            userToCreate = new UserRequest("testeUnitarioLogin",emailTeste, "12345678", rolesString,"12345678951",
                    addressDto, "M", actualDate, "33853056", "33853056",
                    30L, "32", "1.70", "104.0", "latNameTest", "25.0");

            userObject = new User(
                    Long.getLong("301"),
                    "testeUnitarioLogin",
                    emailTeste,
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

            // Authenticate and retrieve JWT token
            jwtToken = obtainAccessToken("admin@admin.com", "12345678");
        }

    private String obtainAccessToken(String username, String password) throws Exception {

        LoginRequest request = new LoginRequest(username, password);
        ObjectMapper mapper = new ObjectMapper();
        //mapper.writeValueAsString(request);

        String loginPayload = mapper.writeValueAsString(request);

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .content(loginPayload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return mapper.readTree(response).get("token").asText();
    }

        @Test
        @Order(1)
        void testCriaUsuarioTeste() throws Exception {
            //given(userService.getPesoIdeal(anyLong())).willReturn("69.225");

            // Create JSON manually

            ObjectMapper mapper = new ObjectMapper();
            String createUserPayload = mapper.writeValueAsString(userToCreate);
            String rotaVerificacao = "/pacientes/getuserbyemail/email/"+emailTeste;

            String resp = mockMvc.perform(MockMvcRequestBuilders.get(rotaVerificacao)
                            .header("Authorization", "Bearer " + jwtToken)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(emailTeste)).toString();

            if(resp == null ) {

                mockMvc.perform(MockMvcRequestBuilders.post("/pacientes/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + jwtToken)
                                .content(createUserPayload)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name", equalTo("testeUnitarioLogin")))
                        .andExpect(jsonPath("$.email", equalTo("testeunitario13@hotmail.com")));
            }else{
                Assertions.assertTrue(true);
            }
        }

        // Perform the POST request and assertions


        @Test
        @Order(2)
        void testGetPesoIdeal() throws Exception {
            //given(userService.getPesoIdeal(anyLong())).willReturn("69.225");

            mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/getPesoIdeal/userId/1/")
                            .header("Authorization", "Bearer " + jwtToken)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("69.22500000000001"));
        }

        @Test
        @Order(3)
        void testGetCpfOfuscado() throws Exception {
            //given(userService.getCpfOfuscado(anyLong())).willReturn("12****789**");

            mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/getCpfOfuscado/userId/1/")
                            .header("Authorization", "Bearer " + jwtToken)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("***456*****"));
        }

        @Test
        @Order(4)
        void testGetSituacaoImc() throws Exception {
            //given(userService.getSituacaoImc(anyLong())).willReturn("Peso normal");

            mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/getSituacaoImc/userId/1/")
                            .header("Authorization", "Bearer " + jwtToken)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Peso normal"));
        }

        @Test
        @Order(5)
        void testGetCpfValido() throws Exception {
           //given(userService.getCpfValido(anyLong())).willReturn("true");

            mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/getCpfValido/userId/1/")
                            .header("Authorization", "Bearer " + jwtToken)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));
        }

        @Test
        @Order(6)
        void testGetCalculaIdade() throws Exception {
            //given(userService.getCalculaIdade(anyLong())).willReturn("34");

            mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/getCalculaIdade/userId/1/")
                            .header("Authorization", "Bearer " + jwtToken)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("0"));
        }

        @Test
        @Order(7)
        void testCalcularIMC() throws Exception {
            //given(userService.calcularIMC(70.0, 1.75)).willReturn(22.86);

            mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/getImc/userId/1/")
                            .header("Authorization", "Bearer " + jwtToken)
                            .param("peso", "70.0")
                            .param("altura", "175")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("22.857142857142858"));
        }
}

