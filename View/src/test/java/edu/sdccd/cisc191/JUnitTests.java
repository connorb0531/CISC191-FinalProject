package edu.sdccd.cisc191;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JUnitTests {
    /*
    MODULE 1 is present in every test (i.e. loops, variables, etc.)
    MODULE 2 has no specific test, although it has implementation in Main.java
    MODULE 3 present in testTask()
    MODULE 4 present in testSaveTasks() & testLoadTasks()
    MODULE 5 is present in every test (i.e. ArrayList)
    MODULE 6 has no specific test, although it has implementation in Main.java (i.e. filteredTasks)
    MODULE 7 is present in testSortTasksByDate() & testSortTasksByDate()
    MODULE 8 is present in TODO...
    MODULE 9 is present in testAutoSave()
    MODULE 10 has no specific test, although it has implementation in Main.java (i.e. event handlers)
     */


    //MODULE 3: OOP
    @Test
    void testTask() {
        LocalDate date = LocalDate.now();
        Task task = new Task("Work", "I have work womp womp", date);

        assertEquals("Work", task.getTaskName());
        assertEquals("I have work womp womp", task.getTaskDescription());
        assertEquals(date, task.getDate());
    }

    //MODULE 4: Data Serialization
    @Test
    void testSaveTasks() {
        TaskSerializer taskSerializer = new TaskSerializer();
        String testFilePath = "test.ser";
        LocalDate date = LocalDate.now();

        Task task1 = new Task("Test 1", "test 1", date);
        Task task2 = new Task("Test 2", "test 2", date);

        ArrayList<Task> testObjects = new ArrayList<>();
        testObjects.add(task1);
        testObjects.add(task2);

        taskSerializer.saveObjects(testObjects, testFilePath);

        assertTrue(new java.io.File(testFilePath).exists());
    }

    //MODULE 4: Data Serialization
    @Test
    void testLoadTasks() {
        TaskSerializer taskSerializer = new TaskSerializer();

        ArrayList<String> tasks = new ArrayList<>();
        tasks.add("Object 1");
        tasks.add("Object 2");

        taskSerializer.saveObjects(tasks, "test.ser");

        ArrayList<String> loadedObjects = taskSerializer.loadObjects("test.ser");

        assertEquals(tasks, loadedObjects);
    }

    //MODULE 7: Sorting
    @Test
    void testSortTasksByDate() {
        ArrayList<Task> taskList = new ArrayList<>();

        Task task1 = new Task("Task 1", "Description 1", LocalDate.of(2023, 7, 1));
        Task task2 = new Task("Task 2", "Description 2", LocalDate.of(2023, 7, 5));
        Task task3 = new Task("Task 3", "Description 3", LocalDate.of(2023, 7, 3));

        taskList.add(task2);
        taskList.add(task3);
        taskList.add(task1);

        Sort.quickSort(taskList);

        assertEquals(task1, taskList.get(0));
        assertEquals(task3, taskList.get(1));
        assertEquals(task2, taskList.get(2));
    }

    //MODULE 7: Searching
    @Test
    void testFilterTaskByName() {
        List<Task> taskList = new LinkedList<>();

        Task task1 = new Task("Task 1", "Description for Task 1", LocalDate.now());
        Task task2 = new Task("Task 2", "Description for Task 2", LocalDate.now());
        Task task3 = new Task("Task 3", "Description for Task 3", LocalDate.now());
        Task task4 = new Task("Task 4", "Description for Task 4", LocalDate.now());

        taskList.add(task1);
        taskList.add(task2);
        taskList.add(task3);
        taskList.add(task4);

        String searchKeyword = "Task 2";
        List<Task> filteredTasks = taskList.stream()
                .filter(task -> task.getTaskName().toLowerCase().contains(searchKeyword.toLowerCase()))
                .collect(Collectors.toList());

        assertEquals(1, filteredTasks.size());
        Task filteredTask = filteredTasks.get(0);
        assertEquals("Task 2", filteredTask.getTaskName());
    }

    //MODULE 9: Concurrency
    @Test
    void testAutoSave() throws InterruptedException {
        TaskSerializer taskSerializer = new TaskSerializer();

        long durationInMillis = 750; //

        ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(new Task("Task 1", "Description for Task 1", LocalDate.now()));
        taskList.add(new Task("Task 2", "Description for Task 2", LocalDate.now()));

        // Calculate the end time for the continuousSaveThread
        long startTime = System.currentTimeMillis();
        long endTime = startTime + durationInMillis;

        Thread continuousSaveThread = new Thread(() -> {
            while (System.currentTimeMillis() < endTime) {
                taskSerializer.saveObjects(taskList, "test.ser");
                try {
                    Thread.sleep(500); // delay between saves (0.5 seconds)
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        });

        continuousSaveThread.setDaemon(true); // will be terminated when the application exits
        continuousSaveThread.start();

        // Give the continuousSaveThread some time to execute
        Thread.sleep(durationInMillis + 1000);

        ArrayList<Task> loadedTasks = taskSerializer.loadObjects("test.ser");

        assertEquals(taskList.size(), loadedTasks.size());

        // Compare individual tasks
        for (int i = 0; i < taskList.size(); i++) {
            Task originalTask = taskList.get(i);
            Task loadedTask = loadedTasks.get(i);

            assertEquals(originalTask.getTaskName(), loadedTask.getTaskName());
            assertEquals(originalTask.getTaskDescription(), loadedTask.getTaskDescription());
            assertEquals(originalTask.getDate(), loadedTask.getDate());
        }
    }
}
