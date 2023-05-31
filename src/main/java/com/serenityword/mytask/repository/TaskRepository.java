package com.serenityword.mytask.repository;

import com.serenityword.mytask.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findTaskById(Long id);

    Task findTaskByTitle(String title);

    Task findTaskByCreatedAt(LocalDateTime createdAt);

    Task findTaskByUpdatedAt(LocalDateTime updatedAt);

    List<Task> findTasksByUsername(String username);
}
