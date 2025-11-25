package sn.groupe3.todo.controller;

import sn.groupe3.todo.model.Task;
import sn.groupe3.todo.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController   // Indique que cette classe expose des endpoints REST
@RequestMapping("/api/tasks")   // URL de base pour toutes les routes de ce controller
@CrossOrigin(origins = "http://localhost:3000")  // Autorise le frontend React à accéder à l'API
public class TaskController {

    // Service qui contient la logique métier
    private final TaskService taskService;

    // Injection du service via le constructeur
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping   // GET /api/tasks
    public List<Task> getAllTasks() {
        // Retourne la liste de toutes les tâches
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")   // GET /api/tasks/id
    public Task getTaskById(@PathVariable Long id) {
        // Retourne la tâche correspondant à l'ID
        return taskService.getTaskById(id);
    }

    @PostMapping   // POST /api/tasks
    public Task createTask(@RequestBody Task task) {
        // Crée une nouvelle tâche à partir du body JSON
        return taskService.createTask(task);
    }

    @PutMapping("/{id}")   // PUT /api/tasks/id
    public Task updateTask(@PathVariable Long id, @RequestBody Task task) {
        // Met à jour une tâche existante
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")   // DELETE /api/tasks/id
    public void deleteTask(@PathVariable Long id) {
        // Supprime la tâche correspondant à l'ID
        taskService.deleteTask(id);
    }
}
