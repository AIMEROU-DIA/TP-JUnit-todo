package sn.groupe3.todo.service;

import sn.groupe3.todo.model.Task;
import java.util.List;

public interface TaskService {

    // Récupérer toutes les tâches
    List<Task> getAllTasks();

    // Créer une nouvelle tâche
    Task createTask(Task task);

    // Mettre à jour une tâche existante
    Task updateTask(Long id, Task updatedTask);

    // Supprimer une tâche par son identifiant
    void deleteTask(Long id);

    // Récupérer une tâche par son identifiant
    Task getTaskById(Long id);
}
