package edu.sdccd.cisc191.JavaFXControls.Label;

import javafx.scene.control.Label;

public class TaskTitleLabel extends Label {
    public TaskTitleLabel(String text) {
        super(text);
        setStyle("-fx-font-size: 22px; -fx-font-family: sans-serif; -fx-font-weight: bold;");
    }
}
