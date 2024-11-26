package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.demo.models.TaskPriority;
import com.example.demo.models.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max=1000)
    @Column(name = "title")
    private String title;

    @Size(max=1000)
    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Status is required")
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Priority is required")
    private TaskPriority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "performers", joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> performers = new ArrayList<>();

    @OneToMany(mappedBy = "task_id", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments  = new ArrayList<>();
    
    public String toString() {
    	return String.format("Task(id: %s; title: %s; status: %s; priority: %s; author: %s)", this.id, this.title, this.status, this.priority, this.author.getId());
    }
    
    
    public void addPerformer(User user) {
    	performers.add(user);
    }
    
    public void deletePerformer(User user) {
    	performers.remove(user);
    }
    
    public void addComment(Comment comment) {
    	comments.add(comment);
    }
}
