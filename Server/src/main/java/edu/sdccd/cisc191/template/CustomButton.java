package edu.sdccd.cisc191.template;

import javafx.scene.control.Button;

public class CustomButton extends Button {
    public CustomButton(String message) {
        super(message);
        setStyle("-fx-padding: 10px; -fx-border-color: black; -fx-border-width: 2px; " +
                "-fx-background-color: #7EA3AC; -fx-text-fill: black;-fx-font: 30px Impact;");
    }
}
