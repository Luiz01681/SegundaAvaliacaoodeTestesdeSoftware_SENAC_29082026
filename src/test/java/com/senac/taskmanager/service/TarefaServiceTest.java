package com.senac.taskmanager.service;

import com.senac.taskmanager.domain.Tarefa;
import com.senac.taskmanager.domain.TaskStatus;
import com.senac.taskmanager.dto.TarefaRequestDTO;
import com.senac.taskmanager.dto.TarefaResponseDTO;
import com.senac.taskmanager.exception.RegraDeNegocioException;
import com.senac.taskmanager.repository.TarefaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {

    @Mock
    private TarefaRepository repository;

    @InjectMocks
    private TarefaService service;

    @Test
    public void criarTarefaComSucesso() {
        TarefaRequestDTO request = new TarefaRequestDTO();
        request.setTitulo("Aprender Spring");
        request.setStatus(TaskStatus.PENDENTE);

        Tarefa mockTarefa = new Tarefa();
        mockTarefa.setId(1L);
        mockTarefa.setTitulo("Aprender Spring");
        mockTarefa.setStatus(TaskStatus.PENDENTE);
        mockTarefa.setDataCriacao(LocalDateTime.now());

        Mockito.when(repository.existsByTitulo(request.getTitulo())).thenReturn(false);
        Mockito.when(repository.save(any(Tarefa.class))).thenReturn(mockTarefa);

        TarefaResponseDTO response = service.criar(request);

        Assertions.assertEquals(1L, response.getId());
        Assertions.assertEquals("Aprender Spring", response.getTitulo());
        Mockito.verify(repository, Mockito.times(1)).save(any(Tarefa.class));
    }

    @Test
    public void atualizarTransitarPendenteParaConcluidaLancaExcecao() {
        TarefaRequestDTO request = new TarefaRequestDTO();
        request.setTitulo("Aprender Spring");
        request.setStatus(TaskStatus.CONCLUIDA);

        Tarefa mockTarefa = new Tarefa();
        mockTarefa.setId(1L);
        mockTarefa.setTitulo("Aprender Spring");
        mockTarefa.setStatus(TaskStatus.PENDENTE);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(mockTarefa));

        Assertions.assertThrows(RegraDeNegocioException.class, () -> service.atualizar(1L, request));
        Mockito.verify(repository, Mockito.never()).save(any(Tarefa.class));
    }
}