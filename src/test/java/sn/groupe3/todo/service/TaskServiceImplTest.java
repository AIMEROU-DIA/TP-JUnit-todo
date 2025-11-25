package sn.groupe3.todo.service;

import sn.groupe3.todo.exception.ResourceNotFoundException;
import sn.groupe3.todo.model.Task;
import sn.groupe3.todo.repository.TaskRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour la classe TaskServiceImpl
 */
class TaskServiceImplTest {

    private TaskRepository taskRepository; // Mock du repository
    private TaskServiceImpl taskService;   // Service à tester

    @BeforeEach
    void setUp() {
        // Avant chaque test : on crée un mock de TaskRepository
        taskRepository = mock(TaskRepository.class);
        // On injecte le mock dans le service
        taskService = new TaskServiceImpl(taskRepository);
    }

    @Test
    void recupererToutesLesTachesDoitRetournerUneListe() {
        // Création d'une liste simulée de tâches
        List<Task> liste = Arrays.asList(
                new Task("T1", "D1", false),
                new Task("T2", "D2", true)
        );

        // Quand findAll() est appelé sur le repository, on renvoie cette liste
        when(taskRepository.findAll()).thenReturn(liste);

        // On appelle la méthode du service
        List<Task> resultat = taskService.getAllTasks();

        // Vérification : la liste retournée contient bien 2 éléments
        assertEquals(2, resultat.size());
        // Vérification : la méthode findAll() a été appelée exactement une fois
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void creerTacheDoitEnregistrerTache() {
        Task tache = new Task("Titre", "Description", false);

        // Quand save() est appelé avec cette tâche, on renvoie la même tâche
        when(taskRepository.save(tache)).thenReturn(tache);

        Task resultat = taskService.createTask(tache);

        // Vérification : la tâche créée n'est pas nulle et a le bon titre
        assertNotNull(resultat);
        assertEquals("Titre", resultat.getTitle());
        // Vérification : save() a été appelé exactement une fois
        verify(taskRepository, times(1)).save(tache);
    }

    @Test
    void creerTacheDoitLeverExceptionSiTacheNull() {
        // Vérifie que si on passe null à createTask, une exception est levée
        assertThrows(IllegalArgumentException.class,
                () -> taskService.createTask(null),
                "La tâche ne peut pas être nulle.");
    }

    @Test
    void recupererTacheParIdDoitRetournerTache() {
        Task tache = new Task("Titre", "Desc", false);
        tache.setId(1L);

        // Quand findById(1L) est appelé, on renvoie la tâche
        when(taskRepository.findById(1L)).thenReturn(Optional.of(tache));

        Task resultat = taskService.getTaskById(1L);

        // Vérifie que l'ID de la tâche retournée est correct
        assertEquals(1L, resultat.getId());
        // Vérifie que findById a été appelé une fois
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void recupererTacheParIdDoitLeverExceptionSiNonTrouvee() {
        // Quand findById(1L) ne trouve rien, renvoie Optional vide
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Vérifie que l'appel lève une ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class,
                () -> taskService.getTaskById(1L));
    }

    @Test
    void mettreAJourTacheDoitMettreAJourCorrectement() {
        Task ancienne = new Task("Ancien", "Ancienne description", false);
        ancienne.setId(1L);

        Task nouvelle = new Task("Nouveau", "Nouvelle description", true);

        // On simule la recherche de la tâche existante
        when(taskRepository.findById(1L)).thenReturn(Optional.of(ancienne));
        // On simule l'enregistrement après mise à jour
        when(taskRepository.save(ArgumentMatchers.any(Task.class))).thenReturn(ancienne);

        Task resultat = taskService.updateTask(1L, nouvelle);

        // Vérifie que les champs ont été mis à jour correctement
        assertEquals("Nouveau", resultat.getTitle());
        assertEquals("Nouvelle description", resultat.getDescription());
        assertTrue(resultat.isCompleted());

        // Vérifie que findById et save ont été appelés
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(ancienne);
    }

    @Test
    void mettreAJourTacheDoitLeverExceptionSiNonTrouvee() {
        Task nouvelle = new Task("Nouveau", "Desc", true);

        // Pas de tâche existante
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Vérifie que l'appel lève ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class,
                () -> taskService.updateTask(1L, nouvelle));
    }

    @Test
    void supprimerTacheDoitSupprimerSiExiste() {
        // Simule que la tâche existe
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);

        // Vérifie que deleteById a été appelé une fois
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void supprimerTacheDoitLeverExceptionSiNonTrouvee() {
        // Simule que la tâche n'existe pas
        when(taskRepository.existsById(1L)).thenReturn(false);

        // Vérifie que deleteTask lève ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class,
                () -> taskService.deleteTask(1L));
    }
}
