package sn.groupe3.todo.service;

import sn.groupe3.todo.exception.ResourceNotFoundException;
import sn.groupe3.todo.model.Task;
import sn.groupe3.todo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    // Repository pour accéder à la base de données
    private final TaskRepository taskRepository;

    // Constructeur pour injecter le repository
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getAllTasks() {
        // Retourne la liste de toutes les tâches
        return taskRepository.findAll();
    }

    @Override
    public Task createTask(Task task) {
        // Vérifie si la tâche est nulle
        if (task == null) {
            throw new IllegalArgumentException("La tâche ne peut pas être nulle.");
        }

        // Enregistre la tâche en base
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, Task updatedTask) {

        // Recherche la tâche et lance une exception si elle n'existe pas
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aucune tâche trouvée avec l'identifiant : " + id
                ));

        // Mise à jour des champs de la tâche
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setCompleted(updatedTask.isCompleted());

        // Sauvegarde de la tâche mise à jour
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {

        // Vérifie si la tâche existe
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Aucune tâche trouvée avec l'identifiant : " + id
            );
        }

        // Supprime la tâche
        taskRepository.deleteById(id);
    }

    @Override
    public Task getTaskById(Long id) {

        // Retourne la tâche si trouvée, sinon lance une exception
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aucune tâche trouvée avec l'identifiant : " + id
                ));
    }
}
