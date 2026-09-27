package com.senac.taskmanager.dto;

import com.senac.taskmanager.domain.TaskStatus;
import java.time.LocalDateTime;

public class TarefaResponseDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private TaskStatus status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }
}