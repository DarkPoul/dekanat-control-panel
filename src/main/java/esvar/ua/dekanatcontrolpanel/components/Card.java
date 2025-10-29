package esvar.ua.dekanatcontrolpanel.components;

import com.vaadin.flow.component.html.Div;

/**
 * A reusable container that renders as a white card with rounded corners and shadow.
 */
public class Card extends Div {

    public Card() {
        addClassName("app-card");
        getStyle().set("background-color", "#ffffff");
        getStyle().set("border-radius", "12px");
        getStyle().set("box-shadow", "0 8px 24px rgba(0,0,0,0.05)");
        getStyle().set("padding", "24px");
        getStyle().set("width", "100%");
        getStyle().set("box-sizing", "border-box");
    }
}
