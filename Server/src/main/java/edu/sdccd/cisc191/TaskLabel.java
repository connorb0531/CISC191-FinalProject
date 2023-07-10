package edu.sdccd.cisc191;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

/**
 * This class provides functionality to change the label's style when the mouse enters or exits the label area,
 * as well as setting a tooltip for displaying additional information.
 */
public class TaskLabel extends Label {
    private static final String defaultStyle = "-fx-font-size: 20px; -fx-font-family: sans-serif;";
    private static final String hoverStyle = "-fx-text-fill: #69cdff; -fx-font-size: 20px; -fx-font-family: sans-serif;";

    /**
     * Constructs a `TaskLabel` object with the specified text.
     *
     * @param text The text to be displayed on the label.
     */
    public TaskLabel(String text) {
        super(text);
        setStyle(defaultStyle);
    }

    /**
     * Sets the label's style to the hover style when the mouse enters the label area,
     * and reverts to the default style when the mouse exits the label area.
     * It also sets a tooltip with the text "Description".
     */
    public void mouseOnLabel() {
        setOnMouseEntered(event -> setStyle(hoverStyle));
        setOnMouseExited(event -> setStyle(defaultStyle));
        setTooltip(new Tooltip("Description"));
    }
}


