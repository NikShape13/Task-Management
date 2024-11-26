package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dao.CommentRepository;
import com.example.demo.dao.TaskRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.dto.CommentDto;
import com.example.demo.dto.TaskDto;
import com.example.demo.models.Comment;
import com.example.demo.models.Role;
import com.example.demo.models.Task;

import com.example.demo.models.TaskPriority;
import com.example.demo.models.TaskStatus;
import com.example.demo.models.User;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final CommentService commentService; 
    private final CommentRepository commentRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, JwtService jwtService, CommentService commentService, CommentRepository commentRepository) {
        this.taskRepository = taskRepository;
		this.userRepository = userRepository;
		this.jwtService = jwtService;
		this.commentService = commentService;
		this.commentRepository = commentRepository;
    }

    public Page<Task> getTaskByIdAndAuthor(Long id, TaskPriority priority, TaskStatus status, Pageable pageable) throws Exception {
        return taskRepository.findTasksForCurrentAuthor(id, priority, status, pageable);
    }

    public Page<Task> getTasksForAssignee(Long assigneeId, TaskPriority priority, TaskStatus status, Pageable pageable) {
        return taskRepository.findTasksForCurrentAssignee(assigneeId, priority, status, pageable);
    }

    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findByStatus(status);
    }

	public Page<Task> getTasksForCurrentUser(Pageable pageable) {
		return null;
	}

	
	public Task getTaskById(Long id, String token) throws Exception {	
		List<String> roles = jwtService.extractRoles(token);
		Long user_id = jwtService.extractId(token);
		
		Task task = taskRepository.getById(id);
		
		boolean isAdmin = roles.stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role));
		
		if(isAdmin != true) {
			boolean check = taskRepository.isPerformer(user_id, task);
			
			if(check != true) {
				throw new Exception();
			}
		}
		
		return task;
	}

	@Transactional
	public Task createTask(TaskDto taskDto) throws Exception {
		Task task = new Task();
		User author = userRepository.getById(taskDto.getAuthorId());
		task.setTitle(taskDto.getTitle());
		task.setDescription(taskDto.getDescription());
		task.setPriority(taskDto.getPriority());
		task.setStatus(taskDto.getStatus());
		task.setAuthor(author);

		

	    for (Long performerId : taskDto.getPerformers()) {
	        
	            User user = userRepository.getById(performerId);
	            
	            if(user==null) {
	            	throw new Exception("User with id "+performerId+" is not exist");
	            }
	            
	            taskRepository.save(task);
	            task.addPerformer(user);

	            
	            userRepository.update(user);
	        
	    }

	    
	    taskRepository.updateTask(task);
	    return task;
	}
	
	@Transactional
	public Task addPerformers(List<Long> performers, Task task) throws Exception {
		for (Long performerId : performers) {
			
	            User user = userRepository.getById(performerId);
	            if(user==null) {
	            	throw new Exception("User with id "+performerId+" is not exist");
	            }
	            
	            if (task.getPerformers().contains(user)) {
	                throw new Exception("User with id " + performerId + " is already added to the task");
	            }

	            task.addPerformer(user);

	            
	            userRepository.update(user);
	        
	    }

	    
	    taskRepository.updateTask(task);
	    return task;
	}
	
	@Transactional
	public Task deletePerformers(List<Long> performers, Task task) {
		for (Long performerId : performers) {
	        try {
	            User user = userRepository.getById(performerId);

	            task.deletePerformer(user);

	            userRepository.update(user);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    taskRepository.updateTask(task);
	    return task;
	}


	@Transactional
	public Task updateTask(Long id, @Valid TaskDto taskDto) {
		Task task = taskRepository.getById(id);
		task.setTitle(taskDto.getTitle()!=null?taskDto.getTitle():task.getTitle());
		task.setDescription(taskDto.getDescription()!=null?taskDto.getDescription():task.getDescription());
		task.setPriority(taskDto.getPriority()!=null?taskDto.getPriority():task.getPriority());
		task.setStatus(taskDto.getStatus()!=null?taskDto.getStatus():task.getStatus());
		taskRepository.updateTask(task);
		return task;
	}
	
	public Task updateTask(Task task) {
		return taskRepository.updateTask(task);
	}

	public void deleteTask(Long id) {
		Task task = taskRepository.getById(id);
		taskRepository.delete(task);
	}

	@Transactional
	public Task updateTaskStatus(Long id, TaskStatus status, String token) throws Exception {
		Task task = taskRepository.getById(id);
		Long user_id = jwtService.extractId(token);
		List<String> roles = jwtService.extractRoles(token);
		
		boolean isAdmin = roles.stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role));
		
		if(isAdmin != true) {
			boolean check = taskRepository.isPerformer(user_id, task);
			
			if(check != true) {
				throw new Exception("Not allowed to change a status");
			}
		}
		
		task.setStatus(status);
		taskRepository.updateTask(task);
		return task;
	}
	
	@Transactional
	public Task updateTaskPriority(Long id, TaskPriority priority) {
		Task task = taskRepository.getById(id);
		task.setPriority(priority);
		taskRepository.updateTask(task);
		return task;
	}

	public Task addCommentToTask(Long id, String comment, String token) throws Exception {
		Task task = taskRepository.getById(id);

		Long user_id = jwtService.extractId(token);

		User user = userRepository.getById(user_id);
		
		List<String> roles = jwtService.extractRoles(token);

		boolean isAdmin = roles.stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role));
		

		if(isAdmin != true) {
			boolean check = taskRepository.isPerformer(user_id, task);
			
			if(check != true) {
				throw new Exception("Not allowed to add a comment");
			}
		}
		Comment newComment = new Comment();
		newComment.setContent(comment);
		newComment.setTask_id(task);
		newComment.setUser_id(user);

        commentRepository.save(newComment);
		
		task.addComment(newComment);
		taskRepository.save(task);

		return task;
	}
	
	public TaskDto makeDto(Task task) {
		TaskDto taskDto = new TaskDto();
		taskDto.setId(task.getId());
    	taskDto.setAuthorId(task.getAuthor().getId());
    	taskDto.setTitle(task.getTitle());
    	taskDto.setDescription(task.getDescription());
    	taskDto.setPriority(task.getPriority());
    	taskDto.setStatus(task.getStatus());
    	taskDto.setPerformers(task.getPerformers().stream().map((User user)->user.getId()).toList());
    	taskDto.setComments(task.getComments().stream().map((Comment comm)->{
    		CommentDto newComment = new CommentDto();
    		newComment.setId(comm.getId());
    		newComment.setContent(comm.getContent());
    		newComment.setTask_id(comm.getTask_id().getId());
    		newComment.setUser_id(comm.getUser_id().getId());
    		return newComment;
    	}).toList());
		return taskDto;
	}
}
