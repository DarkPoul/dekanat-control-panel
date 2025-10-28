package esvar.ua.dekanatcontrolpanel.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Вхід | Control Panel")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm loginForm;
    private final Paragraph message;

    public LoginView() {
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        loginForm = new LoginForm();
        loginForm.setAction("login");
        loginForm.setForgotPasswordButtonVisible(false);
        loginForm.setI18n(createCustomI18n());

        message = new Paragraph();
        message.setVisible(false);

        add(new H1("Control Panel"), loginForm, message);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        boolean error = event.getLocation().getQueryParameters().getParameters().containsKey("error");
        boolean logout = event.getLocation().getQueryParameters().getParameters().containsKey("logout");

        loginForm.setError(error);
        if (error) {
            message.setText("Невірний email або пароль");
            message.setVisible(true);
        } else if (logout) {
            message.setText("Вихід виконано");
            message.setVisible(true);
        } else {
            message.setVisible(false);
        }
    }

    private LoginI18n createCustomI18n() {
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.getForm().setTitle("Вхід");
        i18n.getForm().setUsername("Email");
        i18n.getForm().setPassword("Пароль");
        i18n.getForm().setSubmit("Увійти");
        i18n.getErrorMessage().setTitle("Помилка входу");
        i18n.getErrorMessage().setMessage("Невірний email або пароль");
        return i18n;
    }
}
