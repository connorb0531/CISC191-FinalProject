package edu.sdccd.cisc191.Remote;
// LoggerFactory https://www.slf4j.org/api/org/slf4j/LoggerFactory.html
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// Spring Boot classes.
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
/**
 * ReadTasks main class searches for all tasks and prints to console.
 * TaskRepository retrieves tasks from database and displays results.
 */
@SpringBootApplication
public class ReadTasks
{
    public static final Logger log = LoggerFactory.getLogger(ReadTasks.class);
    public static void main(String[] args)
    {
        SpringApplication.run(ReadTasks.class, args);
    }
    @Bean
    public CommandLineRunner readDemo(TaskRepository taskRepository)
    {
        return (args) ->
        {
            // Fetch all tasks using TaskRepository findAll() method and print to console.
            log.info("All Tasks: ");
            Iterable<TaskDB> tasks = taskRepository.findAll();
            for (TaskDB task: tasks)
            {
                // Return custom representation including task name in lieu of class name and object's hash code.
                String taskName = task.getTaskName();
                log.info("TASK(S): " + taskName);
                // log.info(tasks.toString());
            }
        };
    }
}
