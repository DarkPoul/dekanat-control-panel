package ua.ntu.controlpanel.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.RouterLayout;

public class RootLayout extends AppLayout implements RouterLayout {

    public RootLayout() {
        H1 title = new H1("Control Panel");
        title.getStyle().set("font-size", "1.2rem");
        title.getStyle().set("margin", "0 1rem");

        addToNavbar(title);
    }
}
