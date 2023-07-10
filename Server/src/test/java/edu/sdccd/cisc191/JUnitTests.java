package edu.sdccd.cisc191;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JUnitTests {

    @Test
    void testTask()
    {
        LocalDate date = LocalDate.now();
        Task task = new Task("Work", "I have work womp womp", date);

        assertEquals("Work", task.getTaskName());
        assertEquals("I have work womp womp", task.getTaskDescription());
        assertEquals(date, task.getDate());
    }

    @Test
    void testSaveObjects() {
        DataManager dataManager = new DataManager();
        String testFilePath = "test.ser";
        LocalDate date = LocalDate.now();

        Task task1 = new Task("Test 1", "test 1", date);
        Task task2 = new Task("Test 2", "test 2", date);

        ArrayList<Task> testObjects = new ArrayList<>();
        testObjects.add(task1);
        testObjects.add(task2);

        dataManager.saveObjects(testObjects, testFilePath);

        File file = new File(testFilePath);
        assertTrue(file.exists());
    }

    @Test
    void testLoadObjects() {
        DataManager dataManager = new DataManager();
        String testFilePath = "test.ser";

        ArrayList<String> testObjects = new ArrayList<>();
        testObjects.add("Object 1");
        testObjects.add("Object 2");

        dataManager.saveObjects(testObjects, testFilePath);

        ArrayList<String> loadedObjects = dataManager.loadObjects(testFilePath);

        assertEquals(testObjects, loadedObjects);
    }

    @Test
    void testSortTasksByDate() {
        ArrayList<Task> taskList = new ArrayList<>();

        Task task1 = new Task("Task 1", "Description 1", LocalDate.of(2023, 7, 1));
        Task task2 = new Task("Task 2", "Description 2", LocalDate.of(2023, 7, 5));
        Task task3 = new Task("Task 3", "Description 3", LocalDate.of(2023, 7, 3));

        taskList.add(task2);
        taskList.add(task3);
        taskList.add(task1);

        taskList.sort(Comparator.comparing(Task::getDate));

        assertEquals(task1, taskList.get(0));
        assertEquals(task3, taskList.get(1));
        assertEquals(task2, taskList.get(2));
    }
}
