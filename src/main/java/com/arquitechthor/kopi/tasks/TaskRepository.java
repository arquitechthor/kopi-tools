package com.arquitechthor.kopi.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByOrderByPriorityAsc();

    List<Task> findByCategoryOrderByPriorityAsc(String category);
}
