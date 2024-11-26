package com.example.demo.dao;

import com.example.demo.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.models.TaskPriority;
import com.example.demo.models.TaskStatus;
import com.example.demo.models.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByIdAndAuthorId(Long id, Long authorId);

    List<Task> findByStatus(String status);

    Page<Task> findByAssigneeId(Long assigneeId, Pageable pageable);

    Page<Task> findByAuthorId(Long authorId, Pageable pageable);

    Page<Task> findByAuthorIdOrAssigneeId(Long authorId, Long assigneeId, Pageable pageable);

    Page<Task> findByPriorityAndStatus(TaskPriority priority, TaskStatus status, Pageable pageable);

	Task updateTask(Task task);

	boolean isPerformer(Long user_id, Task task);



	Page<Task> findTasksForCurrentAssignee(Long id, TaskPriority priority, TaskStatus status, Pageable pageable);

	Page<Task> findTasksForCurrentAuthor(Long id, TaskPriority priority, TaskStatus status, Pageable pageable);
}