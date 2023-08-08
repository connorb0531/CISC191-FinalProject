package edu.sdccd.cisc191;

import javafx.geometry.Insets;
import javafx.scene.control.Label;

import java.time.LocalDate;

public class TaskTitle extends Label {
    public TaskTitle(String text) {
        super(text);
        setStyle("-fx-font-size: 22px; -fx-font-family: sans-serif; -fx-font-weight: bold;");
    }
}
