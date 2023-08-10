package edu.sdccd.cisc191.JavaFXControls.Button;

import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * The CloseTaskButton class extends the `Button` class from JavaFX to represent a customized button for closing tasks.
 * It provides a button with an image, specific styles, and a tooltip for deleting a task.
 */
public class CloseTaskButton extends Button {

    /**
     * Constructs a `CloseTaskButton` object.
     * It sets the image, size, style, and tooltip for the button.
     * It also defines the mouse hover effects for the button.
     */
    public CloseTaskButton() {
        Image buttonImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/trash_close.png")));
        ImageView buttonImageView = new ImageView(buttonImage);
        setGraphic(buttonImageView);
        setPrefSize(10, 10);
        setTooltip(new Tooltip("Delete task"));
        setStyle(getButtonStyle());

        setOnMouseEntered(e -> setStyle(getHoverStyle()));
        setOnMouseExited(e -> setStyle(getButtonStyle()));
    }

    private String getButtonStyle() {
        return "-fx-background-color: transparent; -fx-border-color: transparent;";
    }

    private String getHoverStyle() {
        return "-fx-background-color: #ff2b2b; -fx-border-color: transparent;";
    }
}

