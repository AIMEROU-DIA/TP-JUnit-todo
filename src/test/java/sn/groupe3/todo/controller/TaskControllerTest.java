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

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task tache1;

    @BeforeEach
    void setUp() {
        tache1 = new Task("Titre 1", "Desc 1", false);
        tache1.setId(1L);
    }

    @Test
    void recupererToutesLesTaches_doitRetourner200() throws Exception {
        Mockito.when(taskService.getAllTasks()).thenReturn(
                Arrays.asList(tache1)
        );

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Titre 1"));
    }

    @Test
    void recupererTacheParId_doitRetourner200() throws Exception {
        Mockito.when(taskService.getTaskById(1L)).thenReturn(tache1);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Titre 1"));
    }

    @Test
    void recupererTacheParId_doitRetourner404_siNonTrouvee() throws Exception {
        Mockito.when(taskService.getTaskById(1L))
                .thenThrow(new ResourceNotFoundException("Aucune tâche trouvée"));

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void creerTache_doitRetourner201() throws Exception {
        Mockito.when(taskService.createTask(any(Task.class))).thenReturn(tache1);

        mockMvc.perform(
                post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tache1))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Titre 1"));
    }

    @Test
    void mettreAJourTache_doitRetourner200() throws Exception {
        Task tacheMiseAJour = new Task("Titre MAJ", "Desc MAJ", true);
        tacheMiseAJour.setId(1L);

        Mockito.when(taskService.updateTask(eq(1L), any(Task.class)))
                .thenReturn(tacheMiseAJour);

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
        Mockito.doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk());
    }

    @Test
    void supprimerTache_doitRetourner404_siNonTrouvee() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Introuvable"))
                .when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNotFound());
    }
}
