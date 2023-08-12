package edu.sdccd.cisc191;

import edu.sdccd.cisc191.JavaFXControls.Button.*;
import edu.sdccd.cisc191.JavaFXControls.Label.TaskLabel;
import edu.sdccd.cisc191.JavaFXControls.Label.TaskTitleLabel;
import edu.sdccd.cisc191.Local.Sort;
import edu.sdccd.cisc191.Local.Task;
import edu.sdccd.cisc191.Local.TaskSerializer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main extends Application {

    // Global variables for uses in multiple methods
    private Stage stage;
    private final int sceneWidth = 600; // Scenes' width
    private final int sceneHeight = 550; // Scenes' height
    private VBox taskContainer; //stores task gui's
    private TaskTitleLabel taskTitleLabel; // main scene task header
    private ArrayList<Task> taskList = new ArrayList<>(); // stores Task objects
    private ArrayList<Task> completedTasks = new ArrayList<>(); // stores completed Task objects
    List<Task> filteredTasks = new LinkedList<>(); // stores current search tasks
    private final TaskSerializer taskSerializer = new TaskSerializer(); // serializes and deserializes task objects
    private boolean loadTasks = true; // allows for one time task loading
    private boolean isGraphPopupOpen = false; // shows if graph popup is open
    private boolean running = true; // shows state of autoSave method
    private final Axis<String> xAxis = new CategoryAxis(); // completed date x-axis of line chart
    private final NumberAxis yAxis = new NumberAxis(); // number of completed tasks y-axis of line chart
    private final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
    private final TaskGraphManager graphManager = new TaskGraphManager(lineChart);; // manages completed ask graph popup


    /**
     * The WindowView class creates a task manager GUI using JavaFX.
     * <p>
     * This class provides a graphical user interface for managing tasks. Users can
     * add, view, and delete tasks, while the title of the main window dynamically
     * displays the number of tasks. Task data is stored in ArrayLists for active
     * tasks and completed tasks.
     * <p>
     * Features:
     * - Users can add tasks using a popup window with task name, description, and date.
     * - Tasks can be filtered by name using a search input field.
     * - Task descriptions can be viewed in a separate window.
     * - Completed tasks can be viewed, including a graph displaying task completion over time.
     * - Custom controls like TaskTitleLabel, AddButton, CloseTaskButton, and TaskLabel are used.
     * - Task data is auto-saved every 5 seconds to serialized files.
     * <p>
     * Methods:
     * - updateTitle(): Updates the title label to display the number of tasks.
     * - filterTaskByName(String searchKeyword)`: Filters tasks by name and updates the display.
     * - showAddTaskPopup(): Displays a popup window for adding new tasks.
     * - showCompletedTaskGraph(): Displays a graph of completed tasks in a popup.
     * - displayTasks(List<Task> tasks, String type, VBox taskContainer)`: Displays task GUIs.
     * - showTaskDescription(Task task): Displays the description of a task in a separate window.
     * - showCompletedTasks(): Displays completed tasks, including a graph, in a separate window.
     * - autoSave(): Automatically saves task data every 5 seconds.
     * <p>
     * Author: Connor Buckley
     * Email: connorbuckley144@gmail.com
     * Created: 2023-07-09
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        // Labels in top part of scene
        taskTitleLabel = new TaskTitleLabel("No Tasks");
        TextField searchTextField = new TextField();
        searchTextField.setFocusTraversable(false);
        searchTextField.setStyle("-fx-focus-color: #e3c502; -fx-faint-focus-color: transparent ;");
        searchTextField.setPromptText("Search by task name");
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTaskByName(newValue); //takes typed input as param
        });
        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        HBox topNodes = new HBox(taskTitleLabel, topSpacer, searchTextField);
        topNodes.setPadding(new Insets(10));

        // Nodes in the center of scene
        taskContainer = new VBox(10);
        taskContainer.setPadding(new Insets(10));
        ScrollPane taskWindow = new ScrollPane(taskContainer);
        taskWindow.setFitToWidth(true);
        taskWindow.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
        taskWindow.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Nodes in bottom part of scene
        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);
        CompleteTaskButton completeTaskButton = new CompleteTaskButton();
        completeTaskButton.setPrefSize(30, 30);
        completeTaskButton.setOnAction(event -> showCompletedTasks());
        AddButton addButton = new AddButton();
        addButton.setOnAction(event -> showAddTaskPopup()); //popup window asking user for task
        HBox bottomNodes = new HBox(completeTaskButton, bottomSpacer, addButton);
        bottomNodes.setAlignment(Pos.CENTER_RIGHT);
        bottomNodes.setPadding(new Insets(5));

        // Root node set up
        BorderPane root = new BorderPane();
        root.setTop(topNodes);
        root.setCenter(taskWindow);
        root.setBottom(bottomNodes);

        Image stageIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon_logo_B_V2.png")));

        // Deserializes objects to task list from file only once
        if (loadTasks) {
            taskList = taskSerializer.load("tasks.ser");
            completedTasks = taskSerializer.load("completed_tasks.ser");
            displayTasks(taskList, "normal", taskContainer);
            loadTasks = false;
        }

        autoSave(); // saves Task objects every 5 seconds

        //Scene & stage set up
        Scene mainWindow = new Scene(root, sceneWidth, sceneHeight);
        stage.setScene(mainWindow);
        stage.getIcons().add(stageIcon);
        stage.setTitle("Daily Task Manager");
        stage.show();

        // Serializes objects in task list onto file on application close
        stage.setOnCloseRequest(event -> {
            running = false; //stops autosave thread
            taskSerializer.saveObjects(taskList, "tasks.ser");
            taskSerializer.saveObjects(completedTasks, "completed_tasks.ser");
            Platform.exit(); //closes all windows along with main
            System.out.println("Application closed...");
        });
    }

    // Updates scene title base on number of tasks
    private void updateTitle() {
        if (!filteredTasks.isEmpty()) {
            taskTitleLabel.setText("Tasks remaining: " + filteredTasks.size() + "/" + taskList.size());
        } else if (!taskList.isEmpty()) {
            taskTitleLabel.setText("Tasks remaining: " + taskList.size());
        } else {
            taskTitleLabel.setText("No Tasks");
        }
    }

    // Method to filter tasks by name and update the display
    private void filterTaskByName(String searchKeyword) {
        if (searchKeyword.isEmpty()) {
            // If the searchKeyword is empty, clear the filteredTasks list
            filteredTasks.clear();
            displayTasks(taskList, "normal", taskContainer); // Display all tasks from the original list
            taskContainer.getChildren().remove(new Label("   No searched tasks.")); // Remove the "no searched tasks" label if it was added before
        } else {
            // Filter tasks by task name using the provided searchKeyword
            filteredTasks = taskList.stream()
                    .filter(task -> task.getTaskName().toLowerCase().contains(searchKeyword.toLowerCase()))
                    .collect(Collectors.toList());
            displayTasks(filteredTasks, "normal", taskContainer); // Update the display of tasks in the task container based on the filteredTasks list
            // Add "no searched tasks" label if the filtered list is empty
            if (filteredTasks.isEmpty()) {
                taskContainer.getChildren().add(new Label("   No searched tasks."));
            }
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
        TextField taskTextField = new TextField();
        taskTextField.setPromptText("Task name");
        taskTextField.setFocusTraversable(false);
        taskTextField.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        TextArea descriptionTextArea = new TextArea();
        descriptionTextArea.setPromptText("Task description");
        descriptionTextArea.setFocusTraversable(false);
        descriptionTextArea.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        descriptionTextArea.setPrefSize(45, 150);
        descriptionTextArea.setWrapText(true); //grows vertically

        // Date & and time of task entered by user
        DatePicker taskDateCalendar = new DatePicker();
        taskDateCalendar.setFocusTraversable(false);
        taskDateCalendar.setStyle("-fx-focus-color: #e3c502; -fx-faint-focus-color: transparent;");
        taskDateCalendar.setValue(LocalDate.now());

        // Saves user typed task name, description, & date into strings
        SaveButton saveButton = new SaveButton();
        saveButton.setOnAction(e -> {
            String taskName = taskTextField.getText();
            String taskDescription = descriptionTextArea.getText();
            if (descriptionTextArea.getText().isEmpty()) taskDescription = "No description";
            LocalDate taskDate = taskDateCalendar.getValue();

            // Allows new task added ONLY if task text field is not empty
            if (!taskName.isEmpty()) {
                Task task = new Task(taskName, taskDescription, taskDate); //creates new task object to store name and description
                taskList.add(task); //adds to task array list
                displayTasks(taskList, "normal", taskContainer); //displays task GUI's by date
                graphManager.updateGraphData(completedTasks, taskList);
                popupStage.close();
            }
        });

        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);

        // Bottom node container
        HBox bottomNodes = new HBox(taskDateCalendar, bottomSpacer, saveButton);
        bottomNodes.setAlignment(Pos.BOTTOM_RIGHT);
        bottomNodes.setPadding(new Insets(6));

        Region vboxSpacer = new Region();
        VBox.setVgrow(vboxSpacer, Priority.ALWAYS);

        // Popup scene & stage set up
        VBox popupRoot = new VBox(taskTextField, descriptionTextArea, vboxSpacer, bottomNodes);
        Scene popupScene = new Scene(popupRoot, 300, 215);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    private void showCompletedTaskGraph() {
        Platform.runLater(() -> {
            if (!isGraphPopupOpen) {
                isGraphPopupOpen = true;

                // Update and show graph
                graphManager.updateGraphData(completedTasks, taskList);
                graphManager.showGraph(completedTasks, taskList);

                // Set an action to execute when the graph popup is closed
                graphManager.setCloseRequestHandler(() -> {
                    isGraphPopupOpen = false;
                });
            }
        });
    }

    /**
     * Filters the task GUIs by date and updates the task container accordingly.
     *
     * @param taskContainer The VBox container that holds the task GUIs.
     */
    private void displayTasks(List<Task> tasks, String type, VBox taskContainer) {
        // Sorts task objects via date
        Sort.quickSort(tasks);

        // Clears container for all tasks before filtering them by date
        taskContainer.getChildren().clear();
        for (Task task : tasks) {
            HBox taskBox = new HBox(); //container for Task GUI
            if (type.equals("normal")) taskBox.setStyle("-fx-background-color: #dbdbdb");
            if (type.equals("completed")) taskBox.setStyle("-fx-background-color: #91ff80");
            taskBox.setPrefHeight(55);
            taskBox.setAlignment(Pos.CENTER);
            taskBox.setPadding(new Insets(8));

            // Formats date to "Month Day"
            String taskName = task.getTaskName();
            String taskDateString = task.getDate().toString();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(taskDateString, dateFormatter);
            String month = String.valueOf(date.getMonth());
            String formattedDate = month.substring(0, 1).toUpperCase()
                    + month.substring(1).toLowerCase() + " " + date.getDayOfMonth();

            // Task labels
            TaskLabel taskNameLabel = new TaskLabel(" " + taskName);
            taskNameLabel.mouseOnLabel();
            TaskLabel taskDateLabel = new TaskLabel(formattedDate + "  ");

            // Changes and scene to display description of task
            taskNameLabel.setOnMouseClicked(event -> showTaskDescription(task));

            // Adds task object to completeTasks list
            // Removes task GUI component and task object from list
            CompleteTaskButton completeTaskButton = new CompleteTaskButton();
            if (type.equals("normal")) {
                completeTaskButton.setOnAction(event -> {
                    taskContainer.getChildren().remove(taskBox);
                    taskList.remove(task);
                    completedTasks.add(task);
                    task.setDateCompleted(LocalDate.now());
                    graphManager.updateGraphData(completedTasks, taskList);
                    updateTitle();
                });
            }

            // Removes task GUI component and task object from list
            CloseTaskButton closeTaskButton = new CloseTaskButton();
            closeTaskButton.setOnAction(event -> {
                taskContainer.getChildren().remove(taskBox);
                taskList.remove(task);
                completedTasks.remove(task);
                graphManager.updateGraphData(completedTasks, taskList);
                updateTitle();
            });

            // Fills space between task label and close button
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // Task nodes added to hbox, all within vbox

            if (type.equals("normal")) taskBox.getChildren().addAll(completeTaskButton, taskNameLabel, spacer, taskDateLabel, closeTaskButton);
            if (type.equals("completed")) taskBox.getChildren().addAll(taskNameLabel, spacer, taskDateLabel, closeTaskButton);
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
            TaskTitleLabel descriptionTitle = new TaskTitleLabel("Description:");
            HBox topNodes = new HBox(descriptionTitle);
            topNodes.setPadding(new Insets(10));

            //Center nodes
            TaskLabel descriptionLabel = new TaskLabel(task.getTaskDescription());
            descriptionLabel.setWrapText(true);  //make the label wrap the text
            VBox descriptionContainer = new VBox(descriptionLabel);
            descriptionContainer.setPadding(new Insets(20));
            ScrollPane centerWindow = new ScrollPane(descriptionContainer);
            VBox.setVgrow(centerWindow, Priority.ALWAYS);  // Make the ScrollPane grow vertically
            centerWindow.setFitToWidth(true);
            centerWindow.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
            centerWindow.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            // Bottom nodes
            HomeButton mainWindowButton = new HomeButton();
            mainWindowButton.setOnAction(event -> { //switches stage/scene to main and displays tasks
                try {
                    start(stage);
                    displayTasks(taskList, "normal", taskContainer);
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
            Scene descriptionWindow = new Scene(root, sceneWidth, sceneHeight);
            stage.setScene(descriptionWindow);
            stage.show();
        });
    }

    private void showCompletedTasks() {
        Platform.runLater(() -> {
            // Top nodes
            TaskTitleLabel title = new TaskTitleLabel("Completed Tasks");
            HBox descriptionTitleContainer = new HBox(title);
            descriptionTitleContainer.setPadding(new Insets(10));

            //Center nodes
            ScrollPane centerWindow = new ScrollPane(taskContainer);
            VBox.setVgrow(centerWindow, Priority.ALWAYS);  // Make the ScrollPane grow vertically
            centerWindow.setFitToWidth(true);
            centerWindow.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
            centerWindow.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            // Bottom nodes
            HomeButton mainWindowButton = new HomeButton();
            mainWindowButton.setOnAction(event -> { //switches stage/scene to main and displays tasks
                try {
                    start(stage);
                    displayTasks(taskList, "normal",taskContainer);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            GraphButton showGraphButton = new GraphButton();
            showGraphButton.setOnAction(event -> showCompletedTaskGraph()); //shows popup scene of graph (date vs. date completed)

            HBox bottomNodes = new HBox(showGraphButton, spacer, mainWindowButton);
            bottomNodes.setAlignment(Pos.CENTER_RIGHT);
            bottomNodes.setPadding(new Insets(5));

            // Root setup
            BorderPane root = new BorderPane();
            root.setTop(descriptionTitleContainer);
            root.setCenter(centerWindow);
            root.setBottom(bottomNodes);

            displayTasks(completedTasks, "completed", taskContainer);

            if (completedTasks.isEmpty()) {
                taskContainer.getChildren().add(new Label("   No completed tasks."));
            }

            //Scene & stage set up
            Scene scene = new Scene(root, sceneWidth, sceneHeight);
            stage.setScene(scene);
            stage.show();
        });
    }

    /**
     * Creates a new thread for auto-saving Task objects every 5 seconds.
     * This thread continues running as long as the `running` variable is `true`.
     * When the application is closing, the thread is stopped.
     */
    private void autoSave() {
        Thread continuousSaveThread = new Thread(() -> {
            while (running) {
                taskSerializer.saveObjects(taskList, "tasks.ser");
                taskSerializer.saveObjects(completedTasks, "completed_tasks.ser");
                try {
                    Thread.sleep(5000); //delay between saves
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        continuousSaveThread.setDaemon(true); //will be terminated when the application exits
        continuousSaveThread.start();
    }
}