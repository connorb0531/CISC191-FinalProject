package edu.sdccd.cisc191;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class WindowView extends Application {

    // Global variables for uses in multiple methods
    private Stage stage;
    private VBox taskContainer; //stores task gui's
    private TaskTitle taskTitle; // main scene task header
    private ArrayList<Task> taskList = new ArrayList<>(); //stores Task objects
    List<Task> filteredTasks = new LinkedList<>();
    private final DataManager objectSerializer = new DataManager(); // serializes and deserializes task objects
    private boolean showLoadButton = true;

    /**
     * The WindowView class creates a task manager GUI using JavaFX.
     *
     * It provides a main window view where the users can add tasks,
     * view the tasks, and delete them. The number of tasks is displayed
     * in the title of the main window and is updated each time a task is added
     * or removed. The tasks are stored in an ArrayList.
     *
     * When adding a task, a new popup window appears where the user can enter
     * the task's name and description. A task is only added if the task name field
     * is not empty. The task details are stored in a Task object.
     *
     * The program uses several custom controls such as TaskTitle, AddButton,
     * CloseTaskButton, and TaskLabel, which are not defined in this code. Their classes
     * define their looks and behaviors rather than functionality
     *
     * When closing the application, the data of the task objects are saves onto a SER
     * file. And launching the application loads those tasks back.
     *
     * Author: Connor Buckley
     * Email: connorbuckley144@gmail.com
     * Created: 2023-07-09
     *
     */

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        // Labels in top part of scene
        taskTitle = new TaskTitle("No Tasks");
        TextField searchTextField = new TextField();
        searchTextField.setPromptText("Search by task name");
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTaskByName(newValue);
        });
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topNodes = new HBox(taskTitle, spacer, searchTextField);
        topNodes.setPadding(new Insets(10));

        // Nodes in the center of scene
        taskContainer = new VBox(10);
        taskContainer.setPadding(new Insets(10));
        ScrollPane taskWindow = new ScrollPane(taskContainer);
        taskWindow.setFitToWidth(true);
        taskWindow.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
        taskWindow.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Nodes in bottom part of scene
        AddButton addButton = new AddButton();
        addButton.setOnAction(event -> showAddTaskPopup()); //popup window asking user for task
        HBox bottomNodes = new HBox(addButton);
        bottomNodes.setAlignment(Pos.CENTER_RIGHT);
        bottomNodes.setPadding(new Insets(5));

        // Root node set up
        BorderPane root = new BorderPane();
        root.setTop(topNodes);
        root.setCenter(taskWindow);
        root.setBottom(bottomNodes);

        Image stageIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon_logo_B_V2.png")));

        // Deserializes objects to task list from file
        if (showLoadButton) {
            taskList = objectSerializer.loadObjects("tasks.ser");
            displayTasks(taskList, taskContainer);
            showLoadButton = false;
        }

        //Scene & stage set up
        Scene mainWindow = new Scene(root, 500, 500);
        stage.setScene(mainWindow);
        stage.getIcons().add(stageIcon);
        stage.setTitle("Daily Task Manager");
        stage.show();

        // Serializes objects in task list onto file on application close
        stage.setOnCloseRequest(event -> {
            objectSerializer.saveObjects(taskList, "tasks.ser");
            System.out.println("Application closed...");
        });
    }

    // Updates scene title base on number of tasks
    private void updateTitle() {
        boolean isEmpty = taskContainer.getChildren().isEmpty();
        int taskNodeCount = taskContainer.getChildren().size();
        if (isEmpty) {
            taskTitle.setText("No Tasks");
        } else {
            taskTitle.setText( "Tasks remaining: " + taskNodeCount);
        }
    }

    // Method to filter tasks by name and update the display
    private void filterTaskByName(String searchKeyword) {
        // Filter tasks by task name using the provided searchKeyword
        filteredTasks = taskList.stream()
                .filter(task -> task.getTaskName().toLowerCase().contains(searchKeyword.toLowerCase()))
                .collect(Collectors.toList());
        displayTasks(filteredTasks, taskContainer); // Update the display of tasks in the task container based on the filteredTasks list

        // Add "no searched tasks" label if the filtered list is empty
        if (filteredTasks.isEmpty()) {
            taskContainer.getChildren().add(new Label("No searched tasks."));
        }
    }

    //asks user for task details in popup scene
    //creates new container showing details of entered task
    private void showAddTaskPopup() {
        // Popup stage set up
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(stage);
        popupStage.setTitle("Add Task");
        popupStage.setResizable(false);

        // Labels and fields of popup
        Label taskLabel = new Label("Task:");
        TextField taskTextField = new TextField();
        Label descriptionLabel = new Label("Description:");
        TextArea descriptionTextArea = new TextArea();
        descriptionTextArea.setPrefSize(45, 25);
        descriptionTextArea.setWrapText(true); //grows vertically

        // Date & and time of task entered by user
        Label dateLabel = new Label("Date of task:");
        DatePicker taskDateCalendar = new DatePicker();
        taskDateCalendar.setValue(LocalDate.now());

        HBox taskLF = new HBox(5, taskLabel, taskTextField);
        HBox  dateLF = new HBox(5, dateLabel, taskDateCalendar);

        // Saves user typed task name, description, & date into strings
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String taskName = taskTextField.getText();
            String taskDescription = descriptionTextArea.getText();
            if (descriptionTextArea.getText().isEmpty()) taskDescription = "No description";
            LocalDate taskDate = taskDateCalendar.getValue();

            // Allows new task added ONLY if task text field is not empty
            if (!taskName.isEmpty()) {
                Task task = new Task(taskName, taskDescription, taskDate); //creates new task object to store name and description
                taskList.add(task); //adds to task array list
                displayTasks(taskList, taskContainer); //displays task GUI's by date
                popupStage.close();
            }
        });

        // Popup scene & stage set up
        VBox popupRoot = new VBox(15);
        popupRoot.setPadding(new Insets(15));
        popupRoot.getChildren().addAll(taskLF, descriptionLabel, descriptionTextArea, dateLF, saveButton);
        Scene popupScene = new Scene(popupRoot, 300, 215);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    /**
     * Filters the task GUIs by date and updates the task container accordingly.
     *
     * @param taskContainer The VBox container that holds the task GUIs.
     */
    private void displayTasks(List<Task> tasks, VBox taskContainer) {
        // Sorts task objects via date
        tasks.sort(Comparator.comparing(Task::getDate));

        // Clears container for all tasks before filtering them by date
        taskContainer.getChildren().clear();
        for (Task task : tasks) {
            HBox taskBox = new HBox(); //container for Task GUI
            taskBox.setStyle("-fx-background-color: #dbdbdb");
            taskBox.setPrefHeight(55);
            taskBox.setAlignment(Pos.CENTER);
            taskBox.setPadding(new Insets(8));

            // Formats date to MM-dd
            String taskName = task.getTaskName();
            String taskDateString = task.getDate().toString();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(taskDateString, dateFormatter);
            String formattedDate = date.getMonth() + " " + date.getDayOfMonth();

            // Task labels
            TaskLabel taskNameLabel = new TaskLabel(taskName);
            taskNameLabel.mouseOnLabel();
            TaskLabel taskDateLabel = new TaskLabel(formattedDate + "  ");

            // Changes and scene to display description of task
            taskNameLabel.setOnMouseClicked(event -> showTaskDescription(task));

            // Removes task GUI component and task object from list
            CloseTaskButton closeTaskButton = new CloseTaskButton();
            closeTaskButton.setOnAction(event -> {
                taskContainer.getChildren().remove(taskBox);
                taskList.remove(task);
                updateTitle(); //updates title based on # of tasks
            });

            // Fills space between task label and close button
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // Task nodes added to hbox, all within vbox
            taskBox.getChildren().addAll(taskNameLabel, spacer, taskDateLabel, closeTaskButton);
            taskContainer.getChildren().add(taskBox);
            updateTitle();
        }
    }

    /**
     * Displays a window showing the description of a task.
     *
     * @param task The Task object for which the description is to be shown.
     */
    private void showTaskDescription(Task task) {
        Platform.runLater(() -> {
            // Top nodes
            TaskTitle descriptionTitle = new TaskTitle("Description:");
            HBox topNodes = new HBox(descriptionTitle);
            topNodes.setPadding(new Insets(10));

            //Center nodes
            TaskLabel descriptionLabel = new TaskLabel(task.getTaskDescription());
            descriptionLabel.setWrapText(true);  //make the label wrap the text
            VBox descriptionContainer = new VBox(descriptionLabel);
            descriptionContainer.setPrefWidth(497);
            descriptionContainer.setPadding(new Insets(20));
            ScrollPane centerWindow = new ScrollPane(descriptionContainer);
            centerWindow.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
            VBox.setVgrow(centerWindow, Priority.ALWAYS);  // Make the ScrollPane grow vertically

            // Bottom nodes
            Button mainWindowButton = new Button("Back");
            mainWindowButton.setOnAction(event -> { //switches stage/scene to main and displays tasks
                try {
                    start(stage);
                    displayTasks(taskList, taskContainer);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            HBox bottomNodes = new HBox(mainWindowButton);
            bottomNodes.setAlignment(Pos.CENTER_RIGHT);
            bottomNodes.setPadding(new Insets(5));

            // Root setup
            BorderPane root = new BorderPane();
            root.setTop(topNodes);
            root.setCenter(centerWindow);
            root.setBottom(bottomNodes);

            //Scene & stage set up
            Scene descriptionWindow = new Scene(root, 500, 500);
            stage.setScene(descriptionWindow);
            stage.show();
        });
    }

}