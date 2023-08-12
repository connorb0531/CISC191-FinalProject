package edu.sdccd.cisc191.Remote;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TASK")
public class TaskDB implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String taskName;
    private String taskDescription;
    private LocalDate date;
    private Date dateCompleted;

    public TaskDB() {}
    public TaskDB(String taskName, String taskDescription, LocalDate date) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.date = date;
    }

    public String getTaskDescription() {
        return taskDescription;
    }
    public String getTaskName() {
        return taskName;
    }
    public LocalDate getDate() {
        return date;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    @Override
    public String toString() {
        return "Task: " + taskName
                + "  Description: " + taskDescription
                + "  Date: " + date;
    }
}
