package sn.groupe3.todo.model;

import jakarta.persistence.*;

@Entity   // Indique que cette classe représente une table en base de données
public class Task {

    @Id   // Clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-incrémentation
    private Long id;

    // Titre de la tâche
    private String title;

    // Description de la tâche
    private String description;

    // Indique si la tâche est terminée ou non
    private boolean completed;

    // Constructeur vide obligatoire pour JPA
    public Task() {
    }

    // Constructeur pour créer rapidement une tâche
    public Task(String title, String description, boolean completed) {
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
