package edu.sdccd.cisc191.JavaFXControls.Button;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class GraphButton extends Button {
    public GraphButton() {
        Image buttonImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/graph_icon.png")));
        ImageView buttonImageView = new ImageView(buttonImage);
        setGraphic(buttonImageView);
        setPrefSize(20, 20);
        setTooltip(new Tooltip("Show graph"));
        setStyle(getButtonStyle());
        setPadding(new Insets(5));

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
