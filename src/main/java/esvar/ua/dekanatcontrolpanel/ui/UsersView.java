package esvar.ua.dekanatcontrolpanel.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import esvar.ua.dekanatcontrolpanel.components.Card;
import esvar.ua.dekanatcontrolpanel.components.StatusBadge;
import esvar.ua.dekanatcontrolpanel.ui.RootLayout;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDate;
import java.util.List;

@PageTitle("Користувачі | Control Panel")
@Route(value = "users", layout = RootLayout.class)
@PermitAll
public class UsersView extends VerticalLayout {

    public UsersView() {
        setPadding(false);
        setSpacing(false);
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
        getStyle().set("gap", "1.5rem");

        Dialog userDialog = createUserDialog();

        Card usersCard = createUsersCard(userDialog);

        add(usersCard, userDialog);
    }

    private Card createUsersCard(Dialog userDialog) {
        Card card = new Card();
        card.getStyle().set("display", "flex");
        card.getStyle().set("flex-direction", "column");
        card.getStyle().set("gap", "1rem");

        H3 title = new H3("Список користувачів");
        title.getStyle().set("margin", "0");
        title.getStyle().set("color", "#111827");

        Grid<UserEntry> grid = new Grid<>();
        grid.setWidthFull();
        grid.addColumn(UserEntry::getEmail).setHeader("Email").setFlexGrow(1);
        grid.addComponentColumn(entry -> new StatusBadge(entry.getRole(), mapRoleVariant(entry.getRole())))
                .setHeader("Роль").setAutoWidth(true);
        grid.addColumn(entry -> entry.isActive() ? "Активний" : "Заблокований").setHeader("Стан").setAutoWidth(true);
        grid.addColumn(entry -> entry.getCreated().toString()).setHeader("Дата створення").setAutoWidth(true);
        grid.addComponentColumn(entry -> {
            Button edit = new Button(new Icon(VaadinIcon.EDIT));
            edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
            edit.getElement().setProperty("title", "Редагувати");

            Button block = new Button(new Icon(VaadinIcon.BAN));
            block.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
            block.getElement().setProperty("title", "Заблокувати");

            HorizontalLayout actions = new HorizontalLayout(edit, block);
            actions.setSpacing(false);
            actions.getStyle().set("gap", "0.5rem");
            return actions;
        }).setHeader("Дії").setAutoWidth(true).setFlexGrow(0);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setItems(MockData.users());

        Button addUserButton = new Button("Додати користувача", event -> userDialog.open());
        addUserButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout footer = new HorizontalLayout(addUserButton);
        footer.setWidthFull();
        footer.setJustifyContentMode(JustifyContentMode.END);
        footer.setAlignItems(Alignment.CENTER);
        footer.setPadding(false);
        footer.getStyle().set("border-top", "1px solid #e5e7eb");
        footer.getStyle().set("margin", "0 -1.5rem -1.5rem");
        footer.getStyle().set("padding", "1rem 1.5rem 1.5rem");

        card.add(title, grid, footer);

        return card;
    }

    private Dialog createUserDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Новий користувач");
        dialog.setWidth("480px");
        dialog.getElement().getStyle().set("max-width", "90vw");

        TextField emailField = new TextField("Email");
        emailField.setWidthFull();

        Select<String> roleSelect = new Select<>();
        roleSelect.setLabel("Роль");
        roleSelect.setItems("ADMIN", "TEST_MANAGER", "VIEWER");
        roleSelect.setValue("ADMIN");
        roleSelect.setWidthFull();

        Checkbox activeCheckbox = new Checkbox("Активний", true);

        PasswordField passwordField = new PasswordField("Пароль");
        passwordField.setWidthFull();

        FormLayout formLayout = new FormLayout(emailField, roleSelect, activeCheckbox, passwordField);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );

        Button saveButton = new Button("Зберегти");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Скасувати", event -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        dialog.add(formLayout);
        dialog.getFooter().add(cancelButton, saveButton);
        return dialog;
    }

    private StatusBadge.Variant mapRoleVariant(String role) {
        return switch (role) {
            case "ADMIN" -> StatusBadge.Variant.DANGER;
            case "TEST_MANAGER" -> StatusBadge.Variant.WARNING;
            case "VIEWER" -> StatusBadge.Variant.NEUTRAL;
            default -> StatusBadge.Variant.INFO;
        };
    }

    private static class UserEntry {
        private final String email;
        private final String role;
        private final boolean active;
        private final LocalDate created;

        private UserEntry(String email, String role, boolean active, LocalDate created) {
            this.email = email;
            this.role = role;
            this.active = active;
            this.created = created;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }

        public boolean isActive() {
            return active;
        }

        public LocalDate getCreated() {
            return created;
        }
    }

    private static class MockData {
        private static List<UserEntry> users() {
            return List.of(
                    new UserEntry("admin@ntu", "ADMIN", true, LocalDate.of(2024, 1, 15)),
                    new UserEntry("qa@ntu", "TEST_MANAGER", true, LocalDate.of(2024, 3, 22)),
                    new UserEntry("ops@ntu", "ADMIN", false, LocalDate.of(2023, 11, 5)),
                    new UserEntry("viewer@ntu", "VIEWER", true, LocalDate.of(2024, 6, 2))
            );
        }
    }
}
