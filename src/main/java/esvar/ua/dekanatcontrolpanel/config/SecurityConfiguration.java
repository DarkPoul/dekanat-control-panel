package esvar.ua.dekanatcontrolpanel.config;


import com.vaadin.flow.spring.security.VaadinWebSecurity;
import esvar.ua.dekanatcontrolpanel.ui.LoginView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfiguration extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/api/test/status").authenticated()
                .requestMatchers("/api/test/start", "/api/test/stop", "/api/test/restart", "/api/test/update")
                .hasAnyRole("ADMIN", "TEST_MANAGER")
        );

        super.configure(http);
        setLoginView(http, LoginView.class);

        SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler("/");
        successHandler.setAlwaysUseDefaultTargetUrl(true);

        http.formLogin(form -> form
                .successHandler(successHandler)
        );

        // І тільки зараз — HTTPS‑лімітація
//        http.requiresChannel(channel ->
//                channel.anyRequest().requiresSecure()
//        );
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
