package esvar.ua.dekanatcontrolpanel.components;

import com.vaadin.flow.component.html.Span;

/**
 * A small rounded badge used to highlight statuses, roles or severities.
 */
public class StatusBadge extends Span {

    public enum Variant {
        SUCCESS,
        WARNING,
        DANGER,
        NEUTRAL,
        INFO
    }

    public StatusBadge(String text, Variant variant) {
        setText(text);
        addClassName("status-badge");
        getStyle().set("display", "inline-flex");
        getStyle().set("align-items", "center");
        getStyle().set("border-radius", "8px");
        getStyle().set("font-size", "0.75rem");
        getStyle().set("font-weight", "500");
        getStyle().set("padding", "2px 8px");
        applyVariant(variant);
    }

    public void setVariant(Variant variant) {
        applyVariant(variant);
    }

    private void applyVariant(Variant variant) {
        switch (variant) {
            case SUCCESS -> {
                getStyle().set("background-color", "#059669");
                getStyle().set("color", "#ffffff");
            }
            case WARNING -> {
                getStyle().set("background-color", "#fbbf24");
                getStyle().set("color", "#1f2937");
            }
            case DANGER -> {
                getStyle().set("background-color", "#dc2626");
                getStyle().set("color", "#ffffff");
            }
            case NEUTRAL -> {
                getStyle().set("background-color", "#6b7280");
                getStyle().set("color", "#ffffff");
            }
            case INFO -> {
                getStyle().set("background-color", "#2563eb");
                getStyle().set("color", "#ffffff");
            }
        }
    }
}
