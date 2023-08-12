package edu.sdccd.cisc191.Remote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
/*
 * TaskRepository interface, extension of CrudRepository<Tasks, String>.
 */
@Repository
public interface TaskRepository extends CrudRepository<TaskDB, String>
{
    List<TaskDB> findByName(String manufacturerName);
    List<TaskDB> findByDescription(String description);
    List<TaskDB> findByDate(LocalDate date);

}