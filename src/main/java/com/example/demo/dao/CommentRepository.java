package com.example.demo.dao;

import com.example.demo.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByTaskId(Long task_id);

    List<Comment> findByUserId(Long user_id);

    List<Comment> findByTaskIdAndUserId(Long task_id, Long user_id);
}