package esvar.ua.dekanatcontrolpanel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"esvar.ua.dekanatcontrolpanel", "ua.ntu.controlpanel"})
@EntityScan(basePackages = {"ua.ntu.controlpanel.entity"})
@EnableJpaRepositories(basePackages = {"ua.ntu.controlpanel.repo"})
public class ControlPanelApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControlPanelApplication.class, args);
    }
}
