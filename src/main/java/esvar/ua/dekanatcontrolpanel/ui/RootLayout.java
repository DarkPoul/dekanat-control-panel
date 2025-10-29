package esvar.ua.dekanatcontrolpanel.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import esvar.ua.dekanatcontrolpanel.components.StatusBadge;
import esvar.ua.dekanatcontrolpanel.security.SecurityUtils;


public class RootLayout extends AppLayout implements RouterLayout {

    public RootLayout() {
        setPrimarySection(Section.DRAWER);
        createNavbar();
        createDrawer();
    }

    private void createNavbar() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().getStyle().set("color", "#f9fafb");

        H2 title = new H2("Control Panel");
        title.getStyle().set("margin", "0");
        title.getStyle().set("color", "#f9fafb");
        title.getStyle().set("font-size", "1.25rem");

        HorizontalLayout left = new HorizontalLayout(toggle, title);
        left.setAlignItems(FlexComponent.Alignment.CENTER);
        left.setSpacing(true);

        String email = SecurityUtils.getCurrentUserEmail().orElse("admin@ntu");
        String role = resolveRole();

        Span emailSpan = new Span(email);
        emailSpan.getStyle().set("color", "#f9fafb");
        emailSpan.getStyle().set("font-weight", "500");

        StatusBadge roleBadge = new StatusBadge(role, mapRoleToVariant(role));

        Button logoutButton = new Button("Вийти", event -> UI.getCurrent().getPage().setLocation("/logout"));
        logoutButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        logoutButton.getStyle().set("color", "#f9fafb");

        HorizontalLayout userBlock = new HorizontalLayout(emailSpan, roleBadge, logoutButton);
        userBlock.setAlignItems(FlexComponent.Alignment.CENTER);
        userBlock.setSpacing(true);

        HorizontalLayout headerContent = new HorizontalLayout(left, userBlock);
        headerContent.setWidthFull();
        headerContent.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        headerContent.setAlignItems(FlexComponent.Alignment.CENTER);

        Div navBar = new Div(headerContent);
        navBar.getStyle().set("width", "100%");
        navBar.getStyle().set("background-color", "#1f2937");
        navBar.getStyle().set("padding", "0.5rem 1.5rem");

        addToNavbar(navBar);
    }

    private void createDrawer() {
        RouterLink dashboard = new RouterLink("Панель керування", DashboardView.class);
        RouterLink logs = new RouterLink("Журнал помилок", LogsView.class);
        RouterLink users = new RouterLink("Користувачі", UsersView.class);

        dashboard.setHighlightCondition(HighlightConditions.sameLocation());
        logs.setHighlightCondition(HighlightConditions.sameLocation());
        users.setHighlightCondition(HighlightConditions.sameLocation());

        configureDrawerLink(dashboard);
        configureDrawerLink(logs);
        configureDrawerLink(users);

        VerticalLayout navigation = new VerticalLayout(dashboard, logs, users);
        navigation.setPadding(false);
        navigation.setSpacing(false);
        navigation.setWidthFull();
        navigation.getStyle().set("padding", "1rem 0");

        addToDrawer(navigation);
    }

    private void configureDrawerLink(RouterLink link) {
        link.getElement().getStyle().set("display", "block");
        link.getElement().getStyle().set("padding", "0.75rem 1.5rem");
        link.getElement().getStyle().set("color", "#1f2937");
        link.getElement().getStyle().set("font-weight", "500");
        link.getElement().getStyle().set("text-decoration", "none");
    }

    private String resolveRole() {
        if (SecurityUtils.hasRole("ADMIN")) {
            return "ADMIN";
        }
        if (SecurityUtils.hasRole("TEST_MANAGER")) {
            return "TEST_MANAGER";
        }
        if (SecurityUtils.hasRole("VIEWER")) {
            return "VIEWER";
        }
        return "USER";
    }

    private StatusBadge.Variant mapRoleToVariant(String role) {
        return switch (role) {
            case "ADMIN" -> StatusBadge.Variant.DANGER;
            case "TEST_MANAGER" -> StatusBadge.Variant.WARNING;
            case "VIEWER" -> StatusBadge.Variant.NEUTRAL;
            default -> StatusBadge.Variant.INFO;
        };
    }
}