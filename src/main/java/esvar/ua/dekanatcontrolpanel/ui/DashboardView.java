package esvar.ua.dekanatcontrolpanel.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import esvar.ua.dekanatcontrolpanel.components.Card;
import esvar.ua.dekanatcontrolpanel.components.StatusBadge;
import esvar.ua.dekanatcontrolpanel.ui.RootLayout;

import jakarta.annotation.security.PermitAll;

import java.util.List;

@PageTitle("Панель керування | Control Panel")
@Route(value = "", layout = RootLayout.class)
@PermitAll
public class DashboardView extends VerticalLayout {

    public DashboardView() {
        addClassName("dashboard-view");
        setPadding(false);
        setSpacing(false);
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
        getStyle().set("gap", "1.5rem");

        H2 heading = new H2("Панель керування");
        heading.getStyle().set("color", "#111827");
        heading.getStyle().set("margin", "0");

        add(heading, createEnvironmentSection(), createRecentErrorsCard(), createRecentActionsCard());
    }

    private HorizontalLayout createEnvironmentSection() {
        Card testCard = createEnvironmentCard(
                "Тестове середовище",
                new StatusBadge("Активне", StatusBadge.Variant.SUCCESS),
                List.of(
                        info("Версія: v.alfa.1.16.10.2025.2029"),
                        info("Аптайм: 5 днів 12 год"),
                        info("Останнє оновлення: 2025-10-28 14:22 (admin@ntu)"),
                        info("Health: OK")
                ),
                List.of(
                        // TODO: показувати ці кнопки лише для ролей ADMIN/TEST_MANAGER
                        //  (VIEWER не повинен їх бачити або взаємодіяти з ними)
                        primaryButton("Оновити тест"),
                        tertiaryButton("Перезапустити"),
                        tertiaryButton("Запустити"),
                        tertiaryButton("Зупинити")
                )
        );

        Card prodCard = createEnvironmentCard(
                "Продуктове середовище",
                new StatusBadge("Техобслуговування", StatusBadge.Variant.WARNING),
                List.of(
                        info("Версія: v.main.2.4.01"),
                        info("Аптайм: 28 днів 4 год"),
                        info("Останнє оновлення: 2025-10-27 22:15 (prod@ntu)"),
                        info("Health: DEGRADED")
                ),
                List.of(
                        dangerButton("Оновити прод"),
                        tertiaryButton("Перезапустити"),
                        tertiaryButton("Увімкнути"),
                        tertiaryButton("Вимкнути"),
                        dangerButton("Rollback"),
                        tertiaryButton("Тех. обслуговування")
                )
        );

        HorizontalLayout environments = new HorizontalLayout(testCard, prodCard);
        environments.setWidthFull();
        environments.setSpacing(true);
        environments.setPadding(false);
        environments.getStyle().set("gap", "1.5rem");
        environments.setAlignItems(Alignment.STRETCH);
        // TODO responsive: stack vertically on narrow screens
        return environments;
    }

    private Card createEnvironmentCard(String name, StatusBadge statusBadge, List<Span> infoLines, List<Button> actions) {
        Card card = new Card();

        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(Alignment.CENTER);

        H3 title = new H3(name);
        title.getStyle().set("margin", "0");
        title.getStyle().set("color", "#111827");

        header.add(title, statusBadge);

        VerticalLayout infoBlock = new VerticalLayout();
        infoBlock.setSpacing(false);
        infoBlock.setPadding(false);
        infoBlock.getStyle().set("color", "#4b5563");
        infoBlock.getStyle().set("font-size", "0.875rem");
        infoBlock.getStyle().set("gap", "0.25rem");
        infoBlock.add(infoLines.toArray(new Span[0]));

        FlexLayout actionsBlock = new FlexLayout();
        actionsBlock.getStyle().set("gap", "0.5rem");
        actionsBlock.getStyle().set("flex-wrap", "wrap");
        actionsBlock.add(actions.toArray(new Button[0]));

        card.add(header, infoBlock, actionsBlock);
        card.getStyle().set("display", "flex");
        card.getStyle().set("flex-direction", "column");
        card.getStyle().set("gap", "1rem");

        return card;
    }

    private Card createRecentErrorsCard() {
        Card card = new Card();
        card.getStyle().set("display", "flex");
        card.getStyle().set("flex-direction", "column");
        card.getStyle().set("gap", "1rem");

        H3 title = new H3("Останні помилки");
        title.getStyle().set("margin", "0");
        title.getStyle().set("color", "#111827");

        Paragraph subtitle = new Paragraph("5 останніх записів");
        subtitle.getStyle().set("margin", "0");
        subtitle.getStyle().set("color", "#4b5563");
        subtitle.getStyle().set("font-size", "0.875rem");

        Grid<LogEntry> grid = new Grid<>();
        grid.setWidthFull();
        grid.addColumn(LogEntry::getTime).setHeader("Час").setAutoWidth(true);
        grid.addComponentColumn(entry -> new StatusBadge(entry.getEnvironment(), mapEnvironmentVariant(entry.getEnvironment())))
                .setHeader("Середовище").setAutoWidth(true);
        grid.addComponentColumn(entry -> new StatusBadge(entry.getSeverity(), mapSeverityVariant(entry.getSeverity())))
                .setHeader("Рівень").setAutoWidth(true);
        grid.addColumn(LogEntry::getSummary).setHeader("Опис").setFlexGrow(1);
        grid.addComponentColumn(entry -> {
            Button details = new Button("Деталі");
            details.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
            return details;
        }).setHeader(" ").setAutoWidth(true).setFlexGrow(0);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setItems(MockData.recentErrors());
        // TODO: адаптувати відображення для мобільних пристроїв (картки-рядки)

        card.add(title, subtitle, grid);
        return card;
    }

    private Card createRecentActionsCard() {
        Card card = new Card();
        card.getStyle().set("display", "flex");
        card.getStyle().set("flex-direction", "column");
        card.getStyle().set("gap", "1rem");

        H3 title = new H3("Останні дії адміністраторів");
        title.getStyle().set("margin", "0");
        title.getStyle().set("color", "#111827");

        Paragraph subtitle = new Paragraph("5 останніх записів");
        subtitle.getStyle().set("margin", "0");
        subtitle.getStyle().set("color", "#4b5563");
        subtitle.getStyle().set("font-size", "0.875rem");

        Grid<ActionEntry> grid = new Grid<>();
        grid.setWidthFull();
        grid.addColumn(ActionEntry::getTime).setHeader("Час").setAutoWidth(true);
        grid.addColumn(ActionEntry::getUser).setHeader("Користувач").setAutoWidth(true);
        grid.addColumn(ActionEntry::getAction).setHeader("Дія").setAutoWidth(true);
        grid.addComponentColumn(entry -> new StatusBadge(entry.getResult(), mapResultVariant(entry.getResult())))
                .setHeader("Результат").setAutoWidth(true);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setItems(MockData.recentActions());
        // TODO: адаптувати відображення для мобільних пристроїв (картковий список)

        card.add(title, subtitle, grid);
        return card;
    }

    private Span info(String text) {
        Span span = new Span(text);
        span.getStyle().set("color", "#4b5563");
        span.getStyle().set("font-size", "0.875rem");
        return span;
    }

    private Button primaryButton(String label) {
        Button button = new Button(label);
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return button;
    }

    private Button dangerButton(String label) {
        Button button = new Button(label);
        button.addThemeVariants(ButtonVariant.LUMO_ERROR);
        return button;
    }

    private Button tertiaryButton(String label) {
        Button button = new Button(label);
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return button;
    }

    private StatusBadge.Variant mapEnvironmentVariant(String environment) {
        return switch (environment) {
            case "TEST" -> StatusBadge.Variant.INFO;
            case "PROD" -> StatusBadge.Variant.SUCCESS;
            default -> StatusBadge.Variant.NEUTRAL;
        };
    }

    private StatusBadge.Variant mapSeverityVariant(String severity) {
        return switch (severity) {
            case "CRITICAL" -> StatusBadge.Variant.DANGER;
            case "ERROR" -> StatusBadge.Variant.DANGER;
            case "WARN" -> StatusBadge.Variant.WARNING;
            default -> StatusBadge.Variant.INFO;
        };
    }

    private StatusBadge.Variant mapResultVariant(String result) {
        return switch (result) {
            case "SUCCESS" -> StatusBadge.Variant.SUCCESS;
            case "FAILED" -> StatusBadge.Variant.DANGER;
            default -> StatusBadge.Variant.INFO;
        };
    }

    private static class LogEntry {
        private final String time;
        private final String environment;
        private final String severity;
        private final String summary;

        private LogEntry(String time, String environment, String severity, String summary) {
            this.time = time;
            this.environment = environment;
            this.severity = severity;
            this.summary = summary;
        }

        public String getTime() {
            return time;
        }

        public String getEnvironment() {
            return environment;
        }

        public String getSeverity() {
            return severity;
        }

        public String getSummary() {
            return summary;
        }
    }

    private static class ActionEntry {
        private final String time;
        private final String user;
        private final String action;
        private final String result;

        private ActionEntry(String time, String user, String action, String result) {
            this.time = time;
            this.user = user;
            this.action = action;
            this.result = result;
        }

        public String getTime() {
            return time;
        }

        public String getUser() {
            return user;
        }

        public String getAction() {
            return action;
        }

        public String getResult() {
            return result;
        }
    }

    private static class MockData {
        private static List<LogEntry> recentErrors() {
            return List.of(
                    new LogEntry("2025-10-28 16:15", "PROD", "CRITICAL", "Оновлення перервано на кроці міграції БД"),
                    new LogEntry("2025-10-28 15:58", "TEST", "ERROR", "Невдалий деплой сервісу auth"),
                    new LogEntry("2025-10-28 15:43", "PROD", "WARN", "Збільшення часу відповіді API gateway"),
                    new LogEntry("2025-10-28 15:12", "TEST", "ERROR", "Проблема з генерацією звітів"),
                    new LogEntry("2025-10-28 14:55", "PROD", "WARN", "Підвищене навантаження на базу даних")
            );
        }

        private static List<ActionEntry> recentActions() {
            return List.of(
                    new ActionEntry("2025-10-28 16:10", "admin@ntu", "UPDATE_TEST", "SUCCESS"),
                    new ActionEntry("2025-10-28 15:40", "qa@ntu", "RESTART_TEST", "SUCCESS"),
                    new ActionEntry("2025-10-28 15:12", "ops@ntu", "UPDATE_PROD", "FAILED"),
                    new ActionEntry("2025-10-28 14:58", "admin@ntu", "ROLLBACK_PROD", "SUCCESS"),
                    new ActionEntry("2025-10-28 14:20", "viewer@ntu", "VIEW_LOGS", "SUCCESS")
            );
        }
    }
}
