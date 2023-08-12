package edu.sdccd.cisc191.Remote;
// LoggerFactory https://www.slf4j.org/api/org/slf4j/LoggerFactory.html
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

/**
 * InsertTasks main class creates Task instance and saves to database using TaskRepository.
 * Spring application context set up, injecting repository into main class.
 */
@SpringBootApplication
public class InsertTasks
{
    public static final Logger log = LoggerFactory.getLogger(InsertTasks.class);
    public static void main(String[] args)
    {
        SpringApplication.run(InsertTasks.class, args);
    }
    /*
     * Define a Bean named insertDemo that returns a CommandLineRunner.
     * Return lambda expression that implements CommandLineRunner's run method.
     */
    @Bean
    public CommandLineRunner insertDemo(TaskRepository taskRepository)
    {
        return (args) ->
        {
            // Save a few task using TaskRepository save() method.
            taskRepository.save(new TaskDB("Task 1", "Description 1", LocalDate.now()));
            taskRepository.save(new TaskDB("Task 2", "Description 2", LocalDate.now()));
            taskRepository.save(new TaskDB("Task 3", "Description 3", LocalDate.now()));
            taskRepository.save(new TaskDB("Task 4", "Description 4", LocalDate.now()));
            taskRepository.save(new TaskDB("Task 5", "Description 5", LocalDate.now()));
            log.info("TASK(S) SAVED.");
        };
    }
}