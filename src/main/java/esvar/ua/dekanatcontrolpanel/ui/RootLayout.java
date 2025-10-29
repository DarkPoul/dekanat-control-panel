package esvar.ua.dekanatcontrolpanel.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
        RouterLink dashboard = createNavLink(VaadinIcon.DASHBOARD, "Панель керування", DashboardView.class);
        RouterLink logs = createNavLink(VaadinIcon.CLIPBOARD_TEXT, "Журнал помилок", LogsView.class);
        RouterLink users = createNavLink(VaadinIcon.USERS, "Користувачі", UsersView.class);

        Span navTitle = new Span("Навігація");
        navTitle.getStyle().set("font-size", "0.75rem");
        navTitle.getStyle().set("letter-spacing", "0.08em");
        navTitle.getStyle().set("text-transform", "uppercase");
        navTitle.getStyle().set("color", "#6b7280");
        navTitle.getStyle().set("font-weight", "600");
        navTitle.getStyle().set("padding", "0 1rem");

        VerticalLayout navigation = new VerticalLayout(navTitle, dashboard, logs, users);
        navigation.setPadding(false);
        navigation.setSpacing(false);
        navigation.setAlignItems(FlexComponent.Alignment.STRETCH);
        navigation.getStyle().set("padding", "1.5rem 1.25rem");
        navigation.getStyle().set("gap", "0.25rem");
        navigation.getStyle().set("background-color", "#f9fafb");

        addToDrawer(navigation);
    }

    private RouterLink createNavLink(VaadinIcon iconName, String text, Class<? extends Component> navigationTarget) {
        Icon icon = iconName.create();
        icon.setSize("1.25rem");
        icon.getStyle().set("color", "#6b7280");

        Span label = new Span(text);
        label.getStyle().set("font-size", "0.95rem");
        label.getStyle().set("font-weight", "500");
        label.getStyle().set("color", "#374151");

        RouterLink link = new RouterLink();
        link.setRoute(navigationTarget);
        link.add(icon, label);
        link.setHighlightCondition(HighlightConditions.sameLocation());
        link.getElement().getStyle().set("display", "flex");
        link.getElement().getStyle().set("align-items", "center");
        link.getElement().getStyle().set("gap", "0.75rem");
        link.getElement().getStyle().set("padding", "0.75rem 1rem");
        link.getElement().getStyle().set("border-radius", "0.75rem");
        link.getElement().getStyle().set("color", "#1f2937");
        link.getElement().getStyle().set("text-decoration", "none");
        link.getElement().getStyle().set("transition", "background-color 0.2s ease, color 0.2s ease");

        link.setHighlightAction((routerLink, highlight) -> {
            if (highlight) {
                routerLink.getElement().getStyle().set("background-color", "#e0f2fe");
                icon.getStyle().set("color", "#0284c7");
                label.getStyle().set("color", "#0f172a");
                label.getStyle().set("font-weight", "600");
            } else {
                routerLink.getElement().getStyle().set("background-color", "transparent");
                icon.getStyle().set("color", "#6b7280");
                label.getStyle().set("color", "#374151");
                label.getStyle().set("font-weight", "500");
            }
        });

        return link;
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