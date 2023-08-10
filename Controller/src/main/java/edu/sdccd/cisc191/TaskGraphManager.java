package edu.sdccd.cisc191;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskGraphManager {

    private final ScatterChart<String, Number> scatterChart;
    private Stage taskGraphStage;

    public TaskGraphManager(ScatterChart<String, Number> scatterChart) {
        this.scatterChart = scatterChart;
    }

    public void updateGraphData(List<Task> completeTasks) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<String> completionDates = new ArrayList<>();
        for (Task task : completeTasks) {
            completionDates.add(dateFormat.format(task.getDateCompleted()));
        }

        Map<String, Integer> dateTaskCountMap = new HashMap<>();
        for (String date : completionDates) {
            dateTaskCountMap.put(date, dateTaskCountMap.getOrDefault(date, 0) + 1);
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : dateTaskCountMap.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        scatterChart.getData().clear();
        scatterChart.getData().add(series);
    }

    public void showGraph(List<Task> completeTasks) {
        taskGraphStage = new Stage();
        taskGraphStage.initModality(Modality.NONE);
        taskGraphStage.setTitle("Completed Task Graph");
        taskGraphStage.setResizable(false);

        Axis<String> xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setTickUnit(1);

        xAxis.setLabel("Date");
        yAxis.setLabel("Number of Tasks");

        scatterChart.setTitle("Task Completion Dates");
        scatterChart.setLegendVisible(false);

        updateGraphData(completeTasks);

        VBox root = new VBox(scatterChart);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(5, 20, 0, 0));

        Scene popupScene = new Scene(root, 450, 375);
        taskGraphStage.setScene(popupScene);
        taskGraphStage.show();
    }

    public void setCloseRequestHandler(Runnable handler) {
        taskGraphStage.setOnCloseRequest(event -> {
            handler.run();
            taskGraphStage.close();
        });
    }


}
