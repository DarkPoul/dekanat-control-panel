package esvar.ua.dekanatcontrolpanel.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PageTitle("Control Panel Dashboard")
@Route(value = "", layout = RootLayout.class)
@PermitAll
public class DashboardView extends VerticalLayout {

    public DashboardView() {
        setSpacing(true);
        setPadding(true);

        add(
                new H2("Control Panel (TEST only preview)"),
                new Button("Оновити тестове середовище", event -> {
                    // TODO: викликати REST /api/test/update
                }),
                new Button("Статус тестового середовища", event -> {
                    // TODO: викликати REST /api/test/status
                })
        );
    }
}
