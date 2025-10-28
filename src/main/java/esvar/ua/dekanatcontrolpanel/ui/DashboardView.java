package esvar.ua.dekanatcontrolpanel.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import esvar.ua.dekanatcontrolpanel.security.SecurityUtils;
import jakarta.annotation.security.PermitAll;

@PageTitle("Control Panel Dashboard")
@Route(value = "", layout = RootLayout.class)
@PermitAll
public class DashboardView extends VerticalLayout {

    public DashboardView() {
        setSpacing(true);
        setPadding(true);

        String email = SecurityUtils.getCurrentUserEmail().orElse("Невідомий користувач");
        String role;
        if (SecurityUtils.hasRole("ADMIN")) {
            role = "ADMIN";
        } else if (SecurityUtils.hasRole("TEST_MANAGER")) {
            role = "TEST_MANAGER";
        } else if (SecurityUtils.hasRole("VIEWER")) {
            role = "VIEWER";
        } else {
            role = "UNKNOWN";
        }

        Button updateEnvironmentButton = new Button("Оновити тестове середовище", event -> {
            // TODO: викликати REST /api/test/update
        });
        Button statusEnvironmentButton = new Button("Статус тестового середовища", event -> {
            // TODO: викликати REST /api/test/status
        });

        boolean canManageEnvironment = SecurityUtils.hasRole("ADMIN") || SecurityUtils.hasRole("TEST_MANAGER");
        updateEnvironmentButton.setVisible(canManageEnvironment);
        statusEnvironmentButton.setVisible(canManageEnvironment);

        add(
                new H2("Control Panel (TEST only preview)"),
                new Paragraph("Email: " + email),
                new Paragraph("Роль: " + role),
                updateEnvironmentButton,
                statusEnvironmentButton
        );
    }
}
