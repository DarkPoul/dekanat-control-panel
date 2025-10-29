package esvar.ua.dekanatcontrolpanel.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import esvar.ua.dekanatcontrolpanel.components.Card;
import esvar.ua.dekanatcontrolpanel.components.StatusBadge;
import esvar.ua.dekanatcontrolpanel.ui.RootLayout;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@PageTitle("Журнал помилок | Control Panel")
@Route(value = "logs", layout = RootLayout.class)
@PermitAll
public class LogsView extends VerticalLayout {

    public LogsView() {
        setPadding(false);
        setSpacing(false);
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
        getStyle().set("gap", "1.5rem");

        H2 title = new H2("Журнал помилок");
        title.getStyle().set("color", "#111827");
        title.getStyle().set("margin", "0");

        Dialog detailsDialog = createDetailsDialog();

        Card filtersCard = createFiltersCard();
        Card logsCard = createLogsCard(detailsDialog);

        add(title, filtersCard, logsCard, detailsDialog);
    }

    private Card createFiltersCard() {
        Card filters = new Card();
        filters.getStyle().set("display", "flex");
        filters.getStyle().set("flex-direction", "column");
        filters.getStyle().set("gap", "1rem");

        H3 heading = new H3("Фільтри");
        heading.getStyle().set("margin", "0");
        heading.getStyle().set("color", "#111827");

        Select<String> environmentSelect = new Select<>();
        environmentSelect.setLabel("Середовище");
        environmentSelect.setItems("Всі", "TEST", "PROD");
        environmentSelect.setValue("Всі");

        Select<String> levelSelect = new Select<>();
        levelSelect.setLabel("Рівень");
        levelSelect.setItems("Всі", "ERROR", "CRITICAL");
        levelSelect.setValue("Всі");

        DatePicker fromDate = new DatePicker("З дати");
        DatePicker toDate = new DatePicker("До дати");

        Button filterButton = new Button("Фільтрувати");
        filterButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        FormLayout filtersLayout = new FormLayout(environmentSelect, levelSelect, fromDate, toDate, filterButton);
        filtersLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2),
                new FormLayout.ResponsiveStep("900px", 3)
        );
        // TODO: на мобільних пристроях відображати фільтри вертикально на всю ширину

        filters.add(heading, filtersLayout);
        return filters;
    }

    private Card createLogsCard(Dialog detailsDialog) {
        Card card = new Card();
        card.getStyle().set("display", "flex");
        card.getStyle().set("flex-direction", "column");
        card.getStyle().set("gap", "1rem");

        H3 heading = new H3("Записи журналу");
        heading.getStyle().set("margin", "0");
        heading.getStyle().set("color", "#111827");

        Grid<LogEntry> grid = new Grid<>();
        grid.setWidthFull();
        grid.addColumn(LogEntry::getTime).setHeader("Час").setAutoWidth(true);
        grid.addComponentColumn(entry -> new StatusBadge(entry.getEnvironment(), mapEnvironmentVariant(entry.getEnvironment())))
                .setHeader("Середовище").setAutoWidth(true);
        grid.addComponentColumn(entry -> new StatusBadge(entry.getLevel(), mapLevelVariant(entry.getLevel())))
                .setHeader("Рівень").setAutoWidth(true);
        grid.addColumn(LogEntry::getMessage).setHeader("Повідомлення").setFlexGrow(1);
        grid.addComponentColumn(entry -> {
            Button details = new Button("Деталі");
            details.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
            details.addClickListener(event -> showDetailsDialog(entry, detailsDialog));
            return details;
        }).setHeader(" ").setAutoWidth(true).setFlexGrow(0);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setItems(MockData.entries());

        card.add(heading, grid);
        return card;
    }

    private void showDetailsDialog(LogEntry entry, Dialog dialog) {
        dialog.removeAll();
        dialog.add(new H3("Деталі помилки"));
        Pre pre = new Pre(entry.getFullMessage());
        pre.getStyle().set("white-space", "pre-wrap");
        pre.getStyle().set("font-family", "'Fira Code', monospace");
        pre.getStyle().set("font-size", "0.875rem");
        dialog.add(pre);
        dialog.open();
    }

    private Dialog createDetailsDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");
        dialog.getElement().getStyle().set("max-width", "90vw");
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        Button close = new Button("Закрити", event -> dialog.close());
        close.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        dialog.getFooter().add(close);
        return dialog;
    }

    private StatusBadge.Variant mapEnvironmentVariant(String environment) {
        return switch (environment) {
            case "TEST" -> StatusBadge.Variant.INFO;
            case "PROD" -> StatusBadge.Variant.SUCCESS;
            default -> StatusBadge.Variant.NEUTRAL;
        };
    }

    private StatusBadge.Variant mapLevelVariant(String level) {
        return switch (level) {
            case "CRITICAL" -> StatusBadge.Variant.DANGER;
            case "ERROR" -> StatusBadge.Variant.DANGER;
            case "WARN" -> StatusBadge.Variant.WARNING;
            default -> StatusBadge.Variant.INFO;
        };
    }

    private static class LogEntry {
        private final String time;
        private final String environment;
        private final String level;
        private final String message;
        private final String fullMessage;

        private LogEntry(String time, String environment, String level, String message, String fullMessage) {
            this.time = time;
            this.environment = environment;
            this.level = level;
            this.message = message;
            this.fullMessage = fullMessage;
        }

        public String getTime() {
            return time;
        }

        public String getEnvironment() {
            return environment;
        }

        public String getLevel() {
            return level;
        }

        public String getMessage() {
            return message;
        }

        public String getFullMessage() {
            return fullMessage;
        }
    }

    private static class MockData {
        private static List<LogEntry> entries() {
            return List.of(
                    new LogEntry(
                            "2025-10-28 16:15",
                            "PROD",
                            "CRITICAL",
                            "Оновлення перервано на кроці міграції БД",
                            "Traceback: java.sql.SQLException: Timeout while applying migration..."
                    ),
                    new LogEntry(
                            "2025-10-28 15:58",
                            "TEST",
                            "ERROR",
                            "Невдалий деплой сервісу auth",
                            "DeploymentException: Container healthcheck failed after 120s..."
                    ),
                    new LogEntry(
                            "2025-10-28 15:43",
                            "PROD",
                            "WARN",
                            "Збільшення часу відповіді API gateway",
                            "LatencyWarning: Average response time 850ms > threshold"
                    ),
                    new LogEntry(
                            "2025-10-28 15:12",
                            "TEST",
                            "ERROR",
                            "Проблема з генерацією звітів",
                            "ReportException: Missing template for report type 'monthly-summary'"
                    ),
                    new LogEntry(
                            "2025-10-28 14:55",
                            "PROD",
                            "WARN",
                            "Підвищене навантаження на базу даних",
                            "DBLoadWarning: CPU usage 92%, active connections 240"
                    )
            );
        }
    }
}
