package com.example.demo.controllers;

import com.example.demo.dto.CommentDto;
import com.example.demo.dto.PerformersDto;
import com.example.demo.dto.TaskDto;
import com.example.demo.models.Comment;
import com.example.demo.models.Task;
import com.example.demo.models.TaskPriority;
import com.example.demo.models.TaskStatus;
import com.example.demo.models.User;
import com.example.demo.services.CommentService;
import com.example.demo.services.JwtService;
import com.example.demo.services.TaskService;
import com.example.demo.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task Management", description = "Operations related to tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final CommentService commentService; 
    private final JwtService jwtService; 

    @Autowired
    public TaskController(TaskService taskService, UserService userService, CommentService commentService, JwtService jwtService) {
        this.taskService = taskService;
		this.userService = userService;
		this.commentService = commentService;
		this.jwtService = jwtService;
    }

   
    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create task", description = "Allowed only for user with role 'ADMIN' - creates a new task")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto, @RequestHeader("Authorization") String token) {
    	long user_id = jwtService.extractId(token);
    	taskDto.setAuthorId(user_id);
        Task createdTask;
		try {
			createdTask = taskService.createTask(taskDto);
		} catch (Exception e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("info", e.getMessage());
	        return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
		}
        TaskDto newTaskDto = taskService.makeDto(createdTask);
        return new ResponseEntity<>(newTaskDto, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/{task_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete task", description = "Allowed only for user with role 'ADMIN' - deletes a task")
    public ResponseEntity<?> deleteTask(@PathVariable Long task_id, @RequestHeader("Authorization") String token) {
    	Map<String, String> errorResponse = new HashMap<>();
        Task task;
		try {
			task = taskService.getTaskById(task_id, token);
			if(task == null) {
				throw new Exception();
			}
		} catch (Exception e) {
			errorResponse.put("info", "Task is not exist");
	        return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
		}
        
        taskService.deleteTask(task_id);
        errorResponse.put("info", "Task deleted succesfully");
        return new ResponseEntity(errorResponse, HttpStatus.OK);
        
        
    }


    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get task by its id", description = "Open for 'ADMIN' and 'USER' (if user is a performer of this task) - gives a task by id")
    public ResponseEntity<?> getTaskById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
    	try {
	        Task task = taskService.getTaskById(id, token);
	        TaskDto newTaskDto = taskService.makeDto(task);
	        return ResponseEntity.ok(newTaskDto);
	    } catch(Exception e) {
	    	Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("info", "Not allowed to get task or task is not exist");
	    	return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
	    } 
    }
    
    @GetMapping("/author/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get tasks by author id", description = "Accessible to 'ADMIN' and 'USER'. Retrieves tasks assigned to a specific author. If you need a sorting put, for example, 'id,desc' or 'id,asc' in sort")
    public ResponseEntity<?> getTaskByAuthorId(@PathVariable Long id, @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) TaskStatus status, Pageable pageable) {
    	try {
	        Page<Task> tasks = taskService.getTaskByIdAndAuthor(id, priority, status, pageable);
	        Page<TaskDto> taskDtos = tasks.map(taskService::makeDto);
	        return ResponseEntity.ok(taskDtos);
	    } catch(Exception e) {
	    	Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("info", e.getMessage());
	    	return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
	    } 
    }
    
    @GetMapping("/performer/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get tasks by performer id", description = "Accessible to 'ADMIN' and 'USER'. Retrieves tasks assigned to a specific performer. If you need a sorting put, for example, 'id,desc' or 'id,asc' in sort")
    public ResponseEntity<?> getTaskByPerformerId(@PathVariable Long id, @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) TaskStatus status, Pageable pageable) {
    	try {
	        Page<Task> tasks = taskService.getTasksForAssignee(id, priority, status, pageable);
	        Page<TaskDto> taskDtos = tasks.map(taskService::makeDto);
	        return ResponseEntity.ok(taskDtos);
	    } catch(Exception e) {
	    	Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("info", e.getMessage());
	    	return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
	    } 
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update task", description = "Accessible to 'ADMIN' - updates a task")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDto taskDto) {
    	try {
	        Task updatedTask = taskService.updateTask(id, taskDto);
	        TaskDto newTaskDto = taskService.makeDto(updatedTask);
	        return ResponseEntity.ok(newTaskDto);
	    } catch(Exception e) {
	    	Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("info", e.getMessage());
	    	return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
	    } 
    }


    
    @PatchMapping("/{id}/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Update task's status", description = "Accessible to 'ADMIN' and 'USER' - updates a task' status")
    public ResponseEntity<?> updateTaskStatus(@PathVariable Long id, @PathVariable TaskStatus status, @RequestHeader("Authorization") String token) {
        try {
    		Task updatedTask = taskService.updateTaskStatus(id, status, token);
    		TaskDto taskDto = taskService.makeDto(updatedTask);
    		return ResponseEntity.ok(taskDto);
        } catch(Exception e) {
        	Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("info", e.getMessage());
        	return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
        } 
    }
    
    @PatchMapping("/{id}/priority/{priority}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update task's priority", description = "Accessible to 'ADMIN' - updates a task' priority")
    public ResponseEntity<TaskDto> updateTaskPriority(@PathVariable Long id, @PathVariable TaskPriority priority) {
        Task updatedTask = taskService.updateTaskPriority(id, priority);
        TaskDto taskDto = taskService.makeDto(updatedTask);
        return ResponseEntity.ok(taskDto);
    }

    @PostMapping("/{id}/{comment}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Add comment to task", description = "Accessible to 'ADMIN' and 'USER' (if user is a performer of this task)- adds a comment")
    public ResponseEntity<?> addComment(@PathVariable Long id, @PathVariable String comment,  @RequestHeader("Authorization") String token) {
        try {
        	
        	Task updatedTask = taskService.addCommentToTask(id, comment, token);
        	
        	TaskDto taskDto = taskService.makeDto(updatedTask);
        	
    		return ResponseEntity.ok(taskDto);
        } catch(Exception e) {
        	Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("info", e.getMessage());
        	return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
        } 
    }
    
    @PatchMapping("/{task_id}/add_performers")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add performers to task", description = "Accessible to 'ADMIN' - adds performers")
    public ResponseEntity<?> addPerformer(@PathVariable Long task_id, @Valid @RequestBody PerformersDto performersDto, @RequestHeader("Authorization") String token){
    	List<Long> users = new ArrayList<>();
    	for(Long user_id: performersDto.getPerformers()) {
    		users.add(user_id);
    	}
    	Task task;
		try {
			task = taskService.getTaskById(task_id, token);
			task = taskService.addPerformers(users, task);
		} catch (NullPointerException e) {
			Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("info", "Task is not exist");
			return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("info", e.getMessage());
			return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
		}
    	
    	
    	TaskDto newTaskDto = taskService.makeDto(task);
    	
		return ResponseEntity.ok(newTaskDto);
    	
    }
    
    @PatchMapping("/{task_id}/delete_performers")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete performers", description = "Accessible to 'ADMIN' - deletes performers")
    public ResponseEntity<?> deletePerformer(@PathVariable Long task_id, @Valid @RequestBody PerformersDto performersDto, @RequestHeader("Authorization") String token){
    	List<Long> users = new ArrayList<>();
    	for(Long user_id: performersDto.getPerformers()) {
    		users.add(user_id);
    	}
    	Task task;
		try {
			task = taskService.getTaskById(task_id, token);
		} catch (Exception e) {
			Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("info", "Task is not exist");
			return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
		}
		
    	task = taskService.deletePerformers(users, task);
    	
    	TaskDto newTaskDto = taskService.makeDto(task);
    	
		return ResponseEntity.ok(newTaskDto);
    	
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("info", "Access denied: You do not have the required role.");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
}