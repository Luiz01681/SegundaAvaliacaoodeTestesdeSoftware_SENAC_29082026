package com.senac.taskmanager.repository;

import com.senac.taskmanager.domain.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    boolean existsByTitulo(String titulo);
}