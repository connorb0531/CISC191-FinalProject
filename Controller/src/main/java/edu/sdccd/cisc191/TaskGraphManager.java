package edu.sdccd.cisc191;

import edu.sdccd.cisc191.Local.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The TaskGraphManager class manages the creation and display of the task completion graph.
 */
public class TaskGraphManager {

    private final LineChart<String, Number> lineChart;
    private Stage taskGraphStage;

    /**
     * Constructs a TaskGraphManager object with a given LineChart instance.
     *
     * @param lineChart The LineChart instance to manage.
     */
    public TaskGraphManager(LineChart<String, Number> lineChart) {
        this.lineChart = lineChart;
    }

    /**
     * Updates the data in the graph based on completed and current tasks.
     *
     * @param completeTasks The list of completed tasks.
     * @param currentTasks  The list of current tasks.
     */
    public void updateGraphData(List<Task> completeTasks, List<Task> currentTasks) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Gathering completion dates for completed tasks
        List<String> completionDates = new ArrayList<>();
        for (Task task : completeTasks) {
            completionDates.add(dateFormatter.format(task.getDateCompleted()));
        }

        // Counting tasks per date
        Map<String, Integer> dateTaskCountMap = new HashMap<>();
        for (String date : completionDates) {
            dateTaskCountMap.put(date, dateTaskCountMap.getOrDefault(date, 0) + 1);
        }

        XYChart.Series<String, Number> completeSeries = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : dateTaskCountMap.entrySet()) {
            completeSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Similar process for current tasks
        List<String> currentDates = new ArrayList<>();
        for (Task task : currentTasks) {
            currentDates.add(dateFormatter.format(task.getDate()));
        }

        Map<String, Integer> currentDateTaskCountMap = new HashMap<>();
        for (String date : currentDates) {
            currentDateTaskCountMap.put(date, currentDateTaskCountMap.getOrDefault(date, 0) + 1);
        }

        XYChart.Series<String, Number> currentSeries = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : currentDateTaskCountMap.entrySet()) {
            currentSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        completeSeries.setName("Completed Tasks");
        currentSeries.setName("Current Tasks");

        // Clear previous data and add the new series to the chart
        lineChart.getData().clear();
        lineChart.getData().addAll(completeSeries, currentSeries);
    }

    /**
     * Displays the graph of completed and current tasks in a popup window.
     *
     * @param completeTasks The list of completed tasks.
     * @param currentTasks  The list of current tasks.
     */
    public void showGraph(List<Task> completeTasks, List<Task> currentTasks) {
        taskGraphStage = new Stage();
        taskGraphStage.initModality(Modality.WINDOW_MODAL);
        taskGraphStage.setTitle("Completed Task Graph");
        taskGraphStage.setResizable(false);

        Axis<String> xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Date");
        yAxis.setLabel("Number of Tasks");

        lineChart.setTitle("Task Completion Dates");
        lineChart.setLegendVisible(true);
        yAxis.setForceZeroInRange(false);

        updateGraphData(completeTasks, currentTasks);

        VBox root = new VBox(lineChart);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(5, 20, 0, 6));

        Scene popupScene = new Scene(root, 450, 375);
        taskGraphStage.setScene(popupScene);
        taskGraphStage.show();
    }

    /**
     * Sets a handler for the close request event of the graph popup window.
     *
     * @param handler The Runnable to be executed on close request.
     */
    public void setCloseRequestHandler(Runnable handler) {
        taskGraphStage.setOnCloseRequest(event -> {
            handler.run();
            taskGraphStage.close();
        });
    }
}
