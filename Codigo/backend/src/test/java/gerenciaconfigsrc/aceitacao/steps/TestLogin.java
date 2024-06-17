package gerenciaconfigsrc.aceitacao.steps;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gerenciaconfigsrc.ClinicaJavaApplication;
import gerenciaconfigsrc.ClinicaJavaApplicationTests;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import io.cucumber.datatable.DataTable;
import org.junit.jupiter.api.Assertions;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;
import java.util.Map;
import static org.hamcrest.MatcherAssert.assertThat;



@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestLogin.class)
@CucumberContextConfiguration
public class TestLogin{


    @LocalServerPort
    String port;

    ResponseEntity<String> lastResponse;

    private Map<String, String> loginAndPassowrd;
    private String jsonLogin;
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    private int resultado;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @When("the client calls endpoint {string}")
    public void whenClientCalls(String url) {
        try {
            lastResponse = new RestTemplate().exchange("http://localhost:"+port + url, HttpMethod.GET, null,
                    String.class);
        } catch (HttpClientErrorException httpClientErrorException) {
            httpClientErrorException.printStackTrace();
        }
    }

    @Then("response status code is {int}")
    public void thenStatusCodee(int expected) {
        Assertions.assertNotNull(lastResponse);
        Assertions.assertNotNull(lastResponse.getStatusCode());
        assertThat("status code is" + expected,
                lastResponse.getStatusCodeValue() == expected);
    }

    @Then("response status code is not present")
    public void thenStatusCodeeIsNotPresent() {
        Assertions.assertNull(lastResponse);
    }

    @Then("returned string should be {string}")
    public void thenStringIs(String expected) {
        Assertions.assertEquals(expected, lastResponse.getBody());
//        assertThat("Returned string is " + expected,
//                expected.equalsIgnoreCase(lastResponse.getBody()));
    }




    @Given("LoginRequest solicitando login")
    public void loginrequestObjectSolicitandoLogin (DataTable loginData) throws JsonProcessingException {


        ObjectMapper login = new ObjectMapper();
        Map<String, String> map = new HashMap<>(1);

        map.put("email",loginData.row(0).get(0));
        map.put("password",loginData.row(0).get(1));

        //"{\"key\": \"value\"}";

        String jsonStr = login.writeValueAsString(map);

        System.out.println(loginData);
        System.out.println(map.toString());
        System.out.println(jsonStr);

        loginAndPassowrd = map;
        jsonLogin = jsonStr;
    }

    ResponseEntity<String> response;
    @When("quando botao login for pressionado")
    public void pressLogin() throws URISyntaxException, JsonProcessingException {
        String url = "http://localhost:"+ port +"/auth/login";

        List<String> accessControlAllowHeaders = new ArrayList<>();
        String  accessControlAllow = "Origin,X-Requested-With,Content-Type,Accept";
        accessControlAllowHeaders.addAll(List.of(accessControlAllow.split(",")));

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccessControlAllowHeaders(accessControlAllowHeaders);
        headers.setAccessControlAllowOrigin("*");
        headers.setAccessControlRequestMethod(HttpMethod.POST);
        headers.set("Authorization", "Bearer ");


        HttpEntity<String> request = new HttpEntity<String>(jsonLogin, headers);


        //String personResultAsJsonStr = restTemplate.postForObject(url, request, String.class);

        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );
            HttpStatus statusCode = response.getStatusCode();

            if (statusCode == HttpStatus.FOUND) {
                String responseBody = response.getBody();
                //System.out.println("Resposta: " + responseBody);
            } else {
                System.out.println("Erro na requisição: " + statusCode);
            }
            System.out.println("Response from post " + response.getStatusCode());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                System.out.println("Erro de autenticação: Verifique suas credenciais.");
            } else {
                System.out.println("Erro na requisição: " + e.getStatusCode());
            }
        }

        //ResponseEntity personResultAsJsonStr1 = restTemplate.postForObject(url, request, ResponseEntity.class);

        //response = restTemplate.exchange(url,HttpMethod.POST,request,ResponseEntity.class,accessControlAllow).getBody();

        /*JsonNode root = objectMapper.readTree(personResultAsJsonStr);

        Assertions.assertNotNull(personResultAsJsonStr);
        Assertions.assertNotNull(root);
        Assertions.assertNotNull(root.path("name").asText());

        System.out.println("Response from post " + personResultAsJsonStr);*/

        //response = restTemplate.postForObject(new URI(url),jsonLogin,LoginResponse.class);

    }

    @Then("response status code for login is {int}")
    public void responseStatusCodeForLoginIs(int expected) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response);//.getToken());

        if ( response.getStatusCodeValue() != expected)
            throw new RuntimeException("O resultado é " + response.getStatusCodeValue() + ", mas o esperado foi " + expected);
    }



}

