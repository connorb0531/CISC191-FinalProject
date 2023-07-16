package edu.sdccd.cisc191.template;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*; // Alert, Button, Label, TextInputDialog
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import java.util.*; //ArrayList, HashMap, Scanner, Optional

public class Server extends Application {
    private int[][] twoDimArray;
    private final Scanner scanner;
    private User user;
    private Stage primaryStage;

    // Constructor and initialization of variables.
    public Server() {
        twoDimArray = new int[10][10];
        scanner = new Scanner(System.in);
    }

    // Print console menu options. Module 1.
    public void printConsoleMenu() {
        System.out.println("CONSOLE MENU");
        System.out.println("1. GET VALUE AT INDEX");
        System.out.println("2. SET VALUE AT INDEX");
        System.out.println("3. FIND INDEX OF VALUE");
        System.out.println("4. PRINT ALL VALUES");
        System.out.println("5. DELETE VALUE AT INDEX");
        System.out.println("6. EXPAND ARRAY");
        System.out.println("7. SHRINK ARRAY");
        System.out.println("0. EXIT");
    }

    // Handle user input for console menu options.
    public void handleConsoleInput(int choice) {
        switch (choice) {
            case 1:
                getValueAtIndex();
                break;
            case 2:
                setValueAtIndex();
                break;
            case 3:
                findIndicesOfValue();
                break;
            case 4:
                printAllValues();
                break;
            case 5:
                deleteValueAtIndex();
                break;
            case 6:
                expandArray();
                break;
            case 7:
                shrinkArray();
                break;
            case 0:
                System.exit(0);
                break;
            default:
                System.out.println("INVALID OPTION. PLEASE TRY AGAIN.");
                break;
        }
    }

    // Case 1. Get value at specified index in 2D array.
    private void getValueAtIndex() {
        System.out.print("ENTER ROW INDEX");
        int row = scanner.nextInt();
        System.out.print("ENTER COLUMN INDEX");
        int col = scanner.nextInt();
        if (isValidIndex(row, col)) {
            int value = twoDimArray[row][col];
            System.out.println("VALUE AT SET INDEX (" + row + ", " + col + "): " + value);
        } else {
            System.out.println("INVALID INPUT. PLEASE TRY AGAIN.");
        }
    }

    // Case 2. Set value at specified index in 2D array.
    private void setValueAtIndex() {
        System.out.print("ENTER ROW INDEX");
        int row = scanner.nextInt();
        System.out.print("ENTER COLUMN INDEX");
        int col = scanner.nextInt();
        System.out.print("ENTER VALUE");
        int value = scanner.nextInt();
        if (isValidIndex(row, col)) {
            twoDimArray[row][col] = value;
            System.out.println("VALUE AT SET INDEX (" + row + ", " + col + ").");
        } else {
            System.out.println("INVALID INPUT. PLEASE TRY AGAIN.");
        }
    }

    // Case 3. Find the indices of specified value in 2D array.
    private void findIndicesOfValue() {
        System.out.print("ENTER VALUE IN SEARCH OF");
        int value = scanner.nextInt();
        boolean found = false;
        for (int row = 0; row < twoDimArray.length; row++) {
            for (int col = 0; col < twoDimArray[row].length; col++) {
                if (twoDimArray[row][col] == value) {
                    System.out.println("VALUE " + value + " FOUND AT INDEX (" + row + ", " + col + ").");
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("VALUE NOT FOUND");
        }
    }

    // Case 4. Prints all values in 2D array.
    private void printAllValues() {
        System.out.println("ARRAY VALUES");
        for (int row = 0; row < twoDimArray.length; row++) {
            for (int col = 0; col < twoDimArray[row].length; col++) {
                System.out.print(twoDimArray[row][col] + " ");
            }
            System.out.println();
        }
    }

    // Case 5. Deletes value at speified index in 2D array.
    private void deleteValueAtIndex() {
        System.out.print("ENTER ROW INDEX");
        int row = scanner.nextInt();
        System.out.print("ENTER COLUMN INDEX");
        int col = scanner.nextInt();
        if (isValidIndex(row, col)) {
            twoDimArray[row][col] = 0;
            System.out.println("VALUE DELETED AT INDEX (" + row + ", " + col + ").");
        } else {
            System.out.println("INVALID INPUT. PLEASE TRY AGAIN.");
        }
    }

    // Case 6. Expands size of 2D array.
    private void expandArray() {
        int[][] newArray = new int[twoDimArray.length + 1][twoDimArray[0].length + 1];
        for (int row = 0; row < twoDimArray.length; row++) {
            for (int col = 0; col < twoDimArray[row].length; col++) {
                newArray[row][col] = twoDimArray[row][col];
            }
        }
        twoDimArray = newArray;
        System.out.println("ARRAY EXPANDED");
    }

    // Case 7. Shrinks size of 2D array.
    private void shrinkArray() {
        if (twoDimArray.length > 1 && twoDimArray[0].length > 1) {
            int[][] newArray = new int[twoDimArray.length - 1][twoDimArray[0].length - 1];
            for (int row = 0; row < newArray.length; row++) {
                for (int col = 0; col < newArray[row].length; col++) {
                    newArray[row][col] = twoDimArray[row][col];
                }
            }
            twoDimArray = newArray;
            System.out.println("ARRAY SHRUNK");
        } else {
            System.out.println("ARRAY CANNOT BE FURTHER SHRUNK");
        }
    }

    // Check if given row and column indices are valid.
    private boolean isValidIndex(int row, int col) {
        return row >= 0 && row < twoDimArray.length && col >= 0 && col < twoDimArray[row].length;
    }

    // JavaFx method to get value at specified index.
    private void getValueAtIndexGUI() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("GET VALUE");
        dialog.setHeaderText("ENTER INDEXES");
        dialog.setContentText("ROW:");
        Optional<String> rowResult = dialog.showAndWait();
        if (rowResult.isPresent()) {
            dialog.setContentText("COLUMN:");
            Optional<String> colResult = dialog.showAndWait();
            if (colResult.isPresent()) {
                int row = Integer.parseInt(rowResult.get());
                int col = Integer.parseInt(colResult.get());
                if (isValidIndex(row, col)) {
                    int value = twoDimArray[row][col];
                    showAlert("VALUE AT INDEX (" + row + ", " + col + "): " + value);
                } else {
                    showAlert("INVALID INPUT. TRY AGAIN.");
                }
            }
        }
    }

    // JavaFX method to set value at specified index.
    private void setValueAtIndexGUI() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("SET VALUE");
        dialog.setHeaderText("ENTER INDEX");
        dialog.setContentText("ROW:");
        Optional<String> rowResult = dialog.showAndWait();
        if (rowResult.isPresent()) {
            dialog.setContentText("COLUMN:");
            Optional<String> colResult = dialog.showAndWait();
            if (colResult.isPresent()) {
                dialog.setContentText("ENTER VALUE");
                Optional<String> valueResult = dialog.showAndWait();
                if (valueResult.isPresent()) {
                    int row = Integer.parseInt(rowResult.get());
                    int col = Integer.parseInt(colResult.get());
                    int value = Integer.parseInt(valueResult.get());
                    if (isValidIndex(row, col)) {
                        twoDimArray[row][col] = value;
                        showAlert("VALUE SET AT INDEX (" + row + ", " + col + ").");
                    } else {
                        showAlert("INVALID INPUT. TRY AGAIN.");
                    }
                }
            }
        }
    }

    // JavaFX method to find index of specified value.
    private void findIndexOfValueGUI() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("FIND INDEX");
        dialog.setHeaderText("ENTER VALUE TO FIND");
        dialog.setContentText("VALUE:");
        Optional<String> valueResult = dialog.showAndWait();
        if (valueResult.isPresent()) {
            int value = Integer.parseInt(valueResult.get());
            boolean found = false;
            for (int row = 0; row < twoDimArray.length; row++) {
                for (int col = 0; col < twoDimArray[row].length; col++) {
                    if (twoDimArray[row][col] == value) {
                        showAlert("VALUE " + value + " FOUND AT INDEX (" + row + ", " + col + ").");
                        found = true;
                    }
                }
            }
            if (!found) {
                showAlert("VALUE NOT FOUND");
            }
        }
    }

    // JavaFX method to print all values in 2D array.
    private void printAllValuesGUI() {
        StringBuilder sb = new StringBuilder();
        sb.append("ARRAY VALUES \n");
        for (int row = 0; row < twoDimArray.length; row++) {
            for (int col = 0; col < twoDimArray[row].length; col++) {
                sb.append(twoDimArray[row][col]).append(" ");
            }
            sb.append("\n");
        }
        showAlert(sb.toString());
    }

    // JavaFX method to delete value at specified index.
    private void deleteValueAtIndexGUI() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("DELETE VALUE");
        dialog.setHeaderText("ENTER INDEX");
        dialog.setContentText("ROW: ");
        Optional<String> rowResult = dialog.showAndWait();
        if (rowResult.isPresent()) {
            dialog.setContentText("COLUMN: ");
            Optional<String> colResult = dialog.showAndWait();
            if (colResult.isPresent()) {
                int row = Integer.parseInt(rowResult.get());
                int col = Integer.parseInt(colResult.get());
                if (isValidIndex(row, col)) {
                    twoDimArray[row][col] = 0;
                    showAlert("VALUE DELETED AT INDEX (" + row + ", " + col + ").");
                } else {
                    showAlert("INVALID INPUT. TRY AGAIN.");
                }
            }
        }
    }

    // JavaFX method to expand size of 2D array.
    private void expandArrayGUI() {
        int[][] newArray = new int[twoDimArray.length + 1][twoDimArray[0].length + 1];
        for (int row = 0; row < twoDimArray.length; row++) {
            for (int col = 0; col < twoDimArray[row].length; col++) {
                newArray[row][col] = twoDimArray[row][col];
            }
        }
        twoDimArray = newArray;
        showAlert("ARRAY EXPANDED");
    }

    // JavaFX method to shrink size of 2D array.
    private void shrinkArrayGUI() {
        if (twoDimArray.length > 1 && twoDimArray[0].length > 1) {
            int[][] newArray = new int[twoDimArray.length - 1][twoDimArray[0].length - 1];
            for (int row = 0; row < newArray.length; row++) {
                for (int col = 0; col < newArray[row].length; col++) {
                    newArray[row][col] = twoDimArray[row][col];
                }
            }
            twoDimArray = newArray;
            showAlert("ARRAY SHRUNK");
        } else {
            showAlert("ARRAY CANNOT BE FURTHER SHRUNK");
        }
    }

    // Method to show alert or warning with given message.
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ALERT");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveUserPopUp() {
        // Popup stage set up
        Stage saveUserStage = new Stage();
        saveUserStage.initModality(Modality.APPLICATION_MODAL);
        saveUserStage.initOwner(primaryStage);
        saveUserStage.setTitle("Add Task");
        saveUserStage.setResizable(false);

        Label userNameLabel = new Label("Name:");
        TextField userNameTextField = new TextField();

        // Saves user's name and array into user object
        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            String userName = userNameTextField.getText();
            if (!userName.isEmpty()) {
                user = new User(userName, twoDimArray);
                UserSave.saveUser(user, "user.ser"); // saves user object to file
                saveUserStage.close();
            }
        });

        VBox saveUserRoot = new VBox(20, userNameLabel, userNameTextField, saveButton);
        Scene saveUserScene = new Scene(saveUserRoot, 250, 200);
        saveUserStage.setScene(saveUserScene);
        saveUserStage.show();
    }

    // Java FX GUI implementation. Module 2.
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Scene main title
        Label titleLabel = new Label("CONSOLE MENU");
        titleLabel.setStyle("-fx-font: 100px Impact; -fx-font-size: 100px; -fx-font-weight: bold;");
        VBox titleLabelContainer = new VBox(titleLabel);
        titleLabelContainer.setAlignment(Pos.TOP_CENTER);

        // Button setup __________________
        //                                |
        //                                |
        //                                V

        CustomButton getValueButton = new CustomButton("GET VALUE AT INDEX");
        getValueButton.setOnAction(e -> getValueAtIndexGUI());

        CustomButton setValueButton = new CustomButton("SET VALUE AT INDEX");
        setValueButton.setOnAction(e -> setValueAtIndexGUI());

        CustomButton findIndexButton = new CustomButton("FIND VALUE AT INDEX");
        findIndexButton.setOnAction(e -> findIndexOfValueGUI());

        CustomButton printAllButton = new CustomButton("PRINT ALL VALUES");
        printAllButton.setOnAction(e -> printAllValuesGUI());

        CustomButton deleteValueButton = new CustomButton("DELETE VALUE AT INDEX");
        deleteValueButton.setOnAction(e -> deleteValueAtIndexGUI());

        CustomButton expandArrayButton = new CustomButton("EXPAND ARRAY");
        expandArrayButton.setOnAction(e -> expandArrayGUI());

        CustomButton shrinkArrayButton = new CustomButton("SHRINK ARRAY");
        shrinkArrayButton.setOnAction(e -> shrinkArrayGUI());

        // Main Buttons container
        VBox buttonContainer = new VBox(getValueButton, setValueButton, findIndexButton, printAllButton,
                deleteValueButton, expandArrayButton, shrinkArrayButton);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setSpacing(10);
        buttonContainer.setPadding(new Insets(30));

        // Save user button setup
        CustomButton saveUserButton = new CustomButton("SAVE USER DATA");
        saveUserButton.setOnAction(event -> saveUserPopUp());

        // Save button container setup
        HBox saveUserButtonContainer = new HBox(saveUserButton);
        saveUserButtonContainer.setAlignment(Pos.BOTTOM_RIGHT);
        saveUserButtonContainer.setPadding(new Insets(10));
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // sets user object to loaded (if any) object from file
        user = UserSave.loadUser("user.ser");
        System.out.println(user);

        //Scene root setup
        //holds control containers (title label & buttons)
        VBox sceneRoot = new VBox(titleLabelContainer, buttonContainer, spacer, saveUserButtonContainer);
        sceneRoot.setStyle("-fx-background-color: #D3BDA2");

        // Stage & scene setup
        Scene scene = new Scene(sceneRoot, 1600, 900);
        primaryStage.setTitle("CONSOLE MENU");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        /*
         * Without GUI nor networking implementations.
         * Server server = new Server();
         * server.printConsoleMenu();
         * while (true)
         * {
         * System.out.print("Enter your choice: ");
         * int choice = server.scanner.nextInt();
         * server.handleConsoleInput(choice);
         * }
         */
        launch(args);
    }
}


