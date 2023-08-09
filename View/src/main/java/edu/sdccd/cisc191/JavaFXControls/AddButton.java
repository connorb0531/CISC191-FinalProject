package edu.sdccd.cisc191.JavaFXControls;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

/**
 * This class provides a button with a specific shape and style, along with a tooltip for displaying additional information.
 */
public class AddButton extends Button {
    private static final String shape = "M 0 0 L 5 0 L 5 5 L 10 5 L 10 10 L 5 10 L 5 15 L 0 15 L 0 10 L -5 10 L -5 5 L 0 5 Z";

    /**
     * Constructs an `AddButton` object.
     * It sets the preferred size, style, and tooltip for the button.
     * It also defines the mouse hover effects for the button.
     */
    public AddButton() {
        setPrefSize(30, 30);
        setStyle(getButtonStyle());
        setTooltip(new Tooltip("Add task"));

        setOnMouseEntered(e -> setStyle(getHoverStyle()));
        setOnMouseExited(e -> setStyle(getButtonStyle()));
    }

    private String getButtonStyle() {
        return "-fx-shape: '" + shape + "'; -fx-background-color: #e3c502; -fx-border-color: transparent;";
    }

    private String getHoverStyle() {
        return "-fx-shape: '" + shape + "'; -fx-background-color: #ccb100; -fx-border-color: transparent;";
    }
}

