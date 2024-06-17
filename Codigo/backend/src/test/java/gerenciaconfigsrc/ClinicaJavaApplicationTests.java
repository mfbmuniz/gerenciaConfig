package gerenciaconfigsrc;

import gerenciaconfigsrc.aceitacao.steps.TestLogin;
import gerenciaconfigsrc.unitarios.LoginServiceTest;
import gerenciaconfigsrc.unitarios.UserServiceTest;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.platform.suite.api.IncludePackages;
import org.junit.platform.suite.api.SelectPackages;



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
