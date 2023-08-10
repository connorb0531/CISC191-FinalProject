package edu.sdccd.cisc191.JavaFXControls.Button;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class SaveButton extends Button {

    public SaveButton() {
        Image buttonImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/arrow.png")));
        ImageView buttonImageView = new ImageView(buttonImage);
        setGraphic(buttonImageView);
        setPrefSize(20, 20);
        setTooltip(new Tooltip("Save task"));
        setStyle(getButtonStyle());

        setOnMouseEntered(e -> setStyle(getHoverStyle()));
        setOnMouseExited(e -> setStyle(getButtonStyle()));
    }

    private String getButtonStyle() {
        return "-fx-background-color: transparent; -fx-border-color: transparent;";
    }

    private String getHoverStyle() {
        return "-fx-background-color: #dbdbdb; -fx-border-color: transparent;";
    }
}
