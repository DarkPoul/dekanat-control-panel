package esvar.ua.dekanatcontrolpanel;


import esvar.ua.dekanatcontrolpanel.config.TestEnvironmentProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TestEnvironmentProperties.class)
public class ControlPanelApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControlPanelApplication.class, args);
    }
}