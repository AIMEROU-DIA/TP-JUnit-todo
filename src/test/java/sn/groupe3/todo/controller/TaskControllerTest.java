package sn.groupe3.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import sn.groupe3.todo.model.Task;
import sn.groupe3.todo.service.TaskService;
import sn.groupe3.todo.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)  // Test uniquement le controller TaskController
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;   // Permet de simuler des requêtes HTTP vers le controller

    @MockBean
    private TaskService taskService;   // Mock du service pour isoler les tests du controller

    @Autowired
    private ObjectMapper objectMapper;   // Convertit les objets Java en JSON et vice-versa

    private Task tache1;   // Exemple de tâche pour les tests

    @BeforeEach
    void setUp() {
        // Initialise une tâche avant chaque test
        tache1 = new Task("Titre 1", "Desc 1", false);
        tache1.setId(1L);   // ID simulé
    }

    @Test
    void recupererToutesLesTaches_doitRetourner200() throws Exception {
        // Simule le retour du service
        Mockito.when(taskService.getAllTasks()).thenReturn(
                Arrays.asList(tache1)
        );

        // Envoie une requête GET et vérifie le statut et le contenu
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())   // Vérifie que le statut HTTP est 200
                .andExpect(jsonPath("$[0].title").value("Titre 1"));  // Vérifie le contenu JSON
    }

    @Test
    void recupererTacheParId_doitRetourner200() throws Exception {
        // Simule le retour d'une tâche spécifique
        Mockito.when(taskService.getTaskById(1L)).thenReturn(tache1);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Titre 1"));
    }

    @Test
    void recupererTacheParId_doitRetourner404_siNonTrouvee() throws Exception {
        // Simule une exception si la tâche n'existe pas
        Mockito.when(taskService.getTaskById(1L))
                .thenThrow(new ResourceNotFoundException("Aucune tâche trouvée"));

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isNotFound());  // Vérifie le statut HTTP 404
    }

    @Test
    void creerTache_doitRetourner201() throws Exception {
        // Simule la création d'une tâche
        Mockito.when(taskService.createTask(any(Task.class))).thenReturn(tache1);

        // Envoie une requête POST avec la tâche au format JSON
        mockMvc.perform(
                post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tache1))
        )
                .andExpect(status().isOk())  // Vérifie le statut HTTP 200 (ou 201 selon configuration)
                .andExpect(jsonPath("$.title").value("Titre 1"));  // Vérifie le contenu JSON
    }

    @Test
    void mettreAJourTache_doitRetourner200() throws Exception {
        // Crée une tâche mise à jour pour le test
        Task tacheMiseAJour = new Task("Titre MAJ", "Desc MAJ", true);
        tacheMiseAJour.setId(1L);

        // Simule la mise à jour via le service
        Mockito.when(taskService.updateTask(eq(1L), any(Task.class)))
                .thenReturn(tacheMiseAJour);

        // Envoie une requête PUT avec la tâche mise à jour
        mockMvc.perform(
                put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tacheMiseAJour))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Titre MAJ"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void supprimerTache_doitRetourner200() throws Exception {
        // Simule la suppression d'une tâche
        Mockito.doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk());  // Vérifie le statut HTTP 200
    }

    @Test
    void supprimerTache_doitRetourner404_siNonTrouvee() throws Exception {
        // Simule une exception si la tâche à supprimer n'existe pas
        Mockito.doThrow(new ResourceNotFoundException("Introuvable"))
                .when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNotFound());  // Vérifie le statut HTTP 404
    }
}
