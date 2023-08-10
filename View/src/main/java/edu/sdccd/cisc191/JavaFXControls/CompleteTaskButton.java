package edu.sdccd.cisc191.JavaFXControls;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class CompleteTaskButton extends Button {


    public CompleteTaskButton() {
        Image buttonImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/check_mark.png")));
        ImageView buttonImageView = new ImageView(buttonImage);
        setGraphic(buttonImageView);
        setPrefSize(10, 10);
        setTooltip(new Tooltip("Completed task"));
        setStyle(getButtonStyle());
        setPadding(new Insets(5));

        setOnMouseEntered(e -> setStyle(getHoverStyle()));
        setOnMouseExited(e -> setStyle(getButtonStyle()));
    }

    private String getButtonStyle() {
        return "-fx-background-color: transparent; -fx-border-color: transparent;";
    }

    private String getHoverStyle() {
        return "-fx-background-color: #4aff3d; -fx-border-color: transparent;";
    }

}
