package gerenciaconfigsrc;

import gerenciaconfigsrc.aceitacao.steps.TestLogin;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration
@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/java/gerenciaconfigsrc/aceitacao/features",
		glue = "gerenciaconfigsrc.aceitacao.steps",
		plugin = {"pretty"}
)
public class ClinicaJavaApplicationTests {

	/*@Test
	void contextLoads() {
	}*/

}
