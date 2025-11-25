package sn.groupe3.todo.service;

import sn.groupe3.todo.model.Task;
import java.util.List;

public interface TaskService {

    List<Task> getAllTasks();

    Task createTask(Task task);

    Task updateTask(Long id, Task updatedTask);

    void deleteTask(Long id);

    Task getTaskById(Long id);
}
