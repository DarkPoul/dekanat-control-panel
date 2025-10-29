package esvar.ua.dekanatcontrolpanel.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Вхід | Control Panel")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();
    private final Span errorMessage = new Span("Невірний email або пароль");

    public LoginView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background-color", "#f5f6f8");

        configureLoginForm();
        configureErrorMessage();

        Div card = new Div();
        card.getStyle().set("background-color", "#ffffff");
        card.getStyle().set("border-radius", "12px");
        card.getStyle().set("box-shadow", "0 20px 45px rgba(15, 23, 42, 0.12)");
        card.getStyle().set("padding", "24px");
        card.getStyle().set("width", "100%");
        card.getStyle().set("max-width", "360px");
        card.getStyle().set("box-sizing", "border-box");
        card.getStyle().set("display", "flex");
        card.getStyle().set("flex-direction", "column");
        card.getStyle().set("gap", "1rem");

        H1 title = new H1("Control Panel");
        title.getStyle().set("margin", "0");
        title.getStyle().set("text-align", "center");
        title.getStyle().set("color", "#111827");

        Paragraph subtitle = new Paragraph("Увійдіть, щоб продовжити");
        subtitle.getStyle().set("margin", "0");
        subtitle.getStyle().set("text-align", "center");
        subtitle.getStyle().set("color", "#4b5563");

        card.add(title, subtitle, login, errorMessage);
        add(card);
    }

    private void configureLoginForm() {
        login.setAction("login");
        login.setForgotPasswordButtonVisible(false);
        login.getElement().getStyle().set("width", "100%");

        LoginI18n i18n = LoginI18n.createDefault();
        LoginI18n.Form form = i18n.getForm();
        form.setTitle("Вхід");
        form.setUsername("Email");
        form.setPassword("Пароль");
        form.setSubmit("Увійти");
        form.setForgotPassword("Забули пароль?");
        i18n.setForm(form);

        LoginI18n.ErrorMessage error = i18n.getErrorMessage();
        error.setTitle("Помилка входу");
        error.setMessage("Перевірте email та пароль і спробуйте ще раз.");
        i18n.setErrorMessage(error);

        login.setI18n(i18n);
    }

    private void configureErrorMessage() {
        errorMessage.getStyle().set("color", "#dc2626");
        errorMessage.getStyle().set("font-size", "0.875rem");
        errorMessage.getStyle().set("text-align", "center");
        errorMessage.setVisible(false);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            login.setError(true);
            errorMessage.setVisible(true);
        } else {
            login.setError(false);
            errorMessage.setVisible(false);
        }
    }
}