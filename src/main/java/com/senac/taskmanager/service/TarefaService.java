package com.senac.taskmanager.service;

import com.senac.taskmanager.domain.Tarefa;
import com.senac.taskmanager.domain.TaskStatus;
import com.senac.taskmanager.dto.TarefaRequestDTO;
import com.senac.taskmanager.dto.TarefaResponseDTO;
import com.senac.taskmanager.exception.RecursoNaoEncontradoException;
import com.senac.taskmanager.exception.RegraDeNegocioException;
import com.senac.taskmanager.repository.TarefaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TarefaService {

    private final TarefaRepository repository;

    public TarefaService(TarefaRepository repository) {
        this.repository = repository;
    }

    public TarefaResponseDTO criar(TarefaRequestDTO dto) {
        if (repository.existsByTitulo(dto.getTitulo())) {
            throw new RegraDeNegocioException("Não é permitido cadastrar duas tarefas com o mesmo título.");
        }

        if (dto.getStatus() == TaskStatus.CONCLUIDA && dto.getDataConclusao() == null) {
            throw new RegraDeNegocioException("Data de conclusão é obrigatória para tarefas concluídas.");
        }

        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(dto.getTitulo());
        tarefa.setDescricao(dto.getDescricao());
        tarefa.setStatus(dto.getStatus());
        tarefa.setDataConclusao(dto.getDataConclusao());

        Tarefa saved = repository.save(tarefa);
        return mapToDTO(saved);
    }

    public List<TarefaResponseDTO> listar() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public TarefaResponseDTO buscarPorId(Long id) {
        Tarefa tarefa = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada com ID: " + id));
        return mapToDTO(tarefa);
    }

    public TarefaResponseDTO atualizar(Long id, TarefaRequestDTO dto) {
        Tarefa tarefa = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada com ID: " + id));

        if (!tarefa.getTitulo().equals(dto.getTitulo()) && repository.existsByTitulo(dto.getTitulo())) {
            throw new RegraDeNegocioException("Não é permitido atualizar para um título já existente.");
        }

        if (tarefa.getStatus() == TaskStatus.PENDENTE && dto.getStatus() == TaskStatus.CONCLUIDA) {
            throw new RegraDeNegocioException("Não é permitido transitar diretamente de PENDENTE para CONCLUIDA.");
        }

        if (dto.getStatus() == TaskStatus.CONCLUIDA && dto.getDataConclusao() == null) {
            throw new RegraDeNegocioException("Data de conclusão é obrigatória ao concluir a tarefa.");
        }

        tarefa.setTitulo(dto.getTitulo());
        tarefa.setDescricao(dto.getDescricao());
        tarefa.setStatus(dto.getStatus());
        tarefa.setDataConclusao(dto.getDataConclusao());

        Tarefa saved = repository.save(tarefa);
        return mapToDTO(saved);
    }

    public void excluir(Long id) {
        Tarefa tarefa = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada com ID: " + id));

        if (tarefa.getStatus() == TaskStatus.CONCLUIDA) {
            throw new RegraDeNegocioException("Não é possível excluir uma tarefa que já está concluída.");
        }

        repository.delete(tarefa);
    }

    private TarefaResponseDTO mapToDTO(Tarefa tarefa) {
        TarefaResponseDTO dto = new TarefaResponseDTO();
        dto.setId(tarefa.getId());
        dto.setTitulo(tarefa.getTitulo());
        dto.setDescricao(tarefa.getDescricao());
        dto.setStatus(tarefa.getStatus());
        dto.setDataCriacao(tarefa.getDataCriacao());
        dto.setDataConclusao(tarefa.getDataConclusao());
        return dto;
    }
}