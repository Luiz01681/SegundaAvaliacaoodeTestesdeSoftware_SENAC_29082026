package com.senac.taskmanager.controller;

import com.senac.taskmanager.domain.TaskStatus;
import com.senac.taskmanager.dto.TarefaResponseDTO;
import com.senac.taskmanager.service.TarefaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(TarefaController.class)
public class TarefaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TarefaService service;

    @Test
    public void testCriarTarefaEndpoint() throws Exception {
        TarefaResponseDTO responseDTO = new TarefaResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTitulo("Teste Integracao");
        responseDTO.setStatus(TaskStatus.PENDENTE);

        Mockito.when(service.criar(any())).thenReturn(responseDTO);

        String jsonPayload = "{\"titulo\":\"Teste Integracao\",\"status\":\"PENDENTE\"}";

        mockMvc.perform(post("/api/tarefas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Teste Integracao"));
    }

    @Test
    public void testBuscarPorIdEndpoint() throws Exception {
        TarefaResponseDTO responseDTO = new TarefaResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTitulo("Teste Integracao");
        responseDTO.setStatus(TaskStatus.PENDENTE);

        Mockito.when(service.buscarPorId(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/tarefas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Teste Integracao"));
    }
}