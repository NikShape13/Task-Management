package com.example.demo.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.TaskDto;
import com.example.demo.models.Role;
import com.example.demo.models.Task;
import com.example.demo.models.TaskPriority;
import com.example.demo.models.TaskStatus;
import com.example.demo.models.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@Repository
public class TaskRepositoryImpl implements TaskRepository {
	@Autowired
	private EntityManager entityManager;

	@Override
	public void flush() {
		
	}

	@Override
	public <S extends Task> S saveAndFlush(S entity) {
		return null;
	}

	@Override
	public <S extends Task> List<S> saveAllAndFlush(Iterable<S> entities) {
		return null;
	}

	@Override
	public void deleteAllInBatch(Iterable<Task> entities) {
		
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Long> ids) {
		
	}

	@Override
	public void deleteAllInBatch() {
		
	}

	@Override
	public Task getOne(Long id) {
		return null;
	}

	@Override
	public Task getById(Long id) {
		Session session = entityManager.unwrap(Session.class);
		return session.get(Task.class, id);
	}

	@Override
	public Task getReferenceById(Long id) {
		return null;
	}

	@Override
	public <S extends Task> List<S> findAll(Example<S> example) {
		return null;
	}

	@Override
	public <S extends Task> List<S> findAll(Example<S> example, Sort sort) {
		return null;
	}

	@Override
	public <S extends Task> List<S> saveAll(Iterable<S> entities) {
		return null;
	}

	@Override
	public List<Task> findAll() {
		return null;
	}

	@Override
	public List<Task> findAllById(Iterable<Long> ids) {
		return null;
	}

	@Override
	public <S extends Task> S save(S entity) {
		Session session = entityManager.unwrap(Session.class);
		session.save(entity);
		return entity;
	}

	
	@Override
	public Optional<Task> findById(Long id) {
		return Optional.empty();
	}

	@Override
	public boolean existsById(Long id) {
		return false;
	}

	@Override
	public long count() {
		return 0;
	}

	@Override
	public void deleteById(Long id) {
		
	}

	@Override
	public void delete(Task entity) {
		Session session = entityManager.unwrap(Session.class);
		session.delete(entity);
	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {
		
	}

	@Override
	public void deleteAll(Iterable<? extends Task> entities) {
		
	}

	@Override
	public void deleteAll() {
		
	}

	@Override
	public List<Task> findAll(Sort sort) {
		return null;
	}

	@Override
	public Page<Task> findAll(Pageable pageable) {
		return null;
	}

	@Override
	public <S extends Task> Optional<S> findOne(Example<S> example) {
		return Optional.empty();
	}

	@Override
	public <S extends Task> Page<S> findAll(Example<S> example, Pageable pageable) {
		return null;
	}

	@Override
	public <S extends Task> long count(Example<S> example) {
		return 0;
	}

	@Override
	public <S extends Task> boolean exists(Example<S> example) {
		return false;
	}

	@Override
	public <S extends Task, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		return null;
	}

	@Override
	public Optional<Task> findByIdAndAuthorId(Long id, Long authorId) {
		return Optional.empty();
	}

	@Override
	public List<Task> findByStatus(String status) {
		return null;
	}

	
	public Page<Task> findByAssigneeId(Long user_id, Pageable pageable) {
		Session session = entityManager.unwrap(Session.class);
	    Query query = session.createNativeQuery(
	        "SELECT t.* FROM tasks t JOIN performers p ON t.id = p.task_id WHERE p.user_id = :user_id", 
	        Task.class
	    );
	    query.setParameter("user_id", user_id);
	    
	    query.setFirstResult((int) pageable.getOffset());
	    query.setMaxResults(pageable.getPageSize());

	    List<Task> tasks = query.getResultList();

	    Query countQuery = session.createNativeQuery(
	        "SELECT COUNT(t.*) FROM tasks t JOIN performers p ON t.id = p.task_id WHERE p.user_id = :user_id",
	        Long.class
	    );
	    countQuery.setParameter("user_id", user_id);
	    long total = (long) countQuery.getSingleResult();

	    return new PageImpl<>(tasks, pageable, total);
	}

	@Override
	public Page<Task> findByAuthorId(Long authorId, Pageable pageable) {
		return null;
	}

	@Override
	public Page<Task> findByAuthorIdOrAssigneeId(Long authorId, Long assigneeId, Pageable pageable) {
		return null;
	}

	@Override
	public Page<Task> findTasksForCurrentAuthor(Long id, TaskPriority priority, TaskStatus status, Pageable pageable) {
	    StringBuilder queryStr = new StringBuilder("SELECT t FROM Task t WHERE t.author.id = :user_id");
	    
	    if (priority != null) {
	        queryStr.append(" AND t.priority = :priority");
	    }

	    if (status != null) {
	        queryStr.append(" AND t.status = :status");
	    }

	    if (pageable.getSort() != null && pageable.getSort().isSorted()) {
	        queryStr.append(" ORDER BY ");
	        pageable.getSort().forEach(order -> {
	            queryStr.append("t." + order.getProperty() + " " + (order.isAscending() ? "ASC" : "DESC") + ", ");
	        });
	        queryStr.delete(queryStr.length() - 2, queryStr.length());
	    }

	    Query query = entityManager.createQuery(queryStr.toString(), Task.class);
	    query.setParameter("user_id", id);
	    if (priority != null) {
	        query.setParameter("priority", priority);
	    }
	    if (status != null) {
	        query.setParameter("status", status);
	    }
	    
	    query.setFirstResult((int) pageable.getOffset());
	    query.setMaxResults(pageable.getPageSize());

	    List<Task> taskList = query.getResultList();

	    StringBuilder countQueryStr = new StringBuilder("SELECT COUNT(t) FROM Task t WHERE t.author.id = :user_id");
	    if (priority != null) {
	        countQueryStr.append(" AND t.priority = :priority");
	    }
	    if (status != null) {
	        countQueryStr.append(" AND t.status = :status");
	    }

	    Query countQuery = entityManager.createQuery(countQueryStr.toString());
	    countQuery.setParameter("user_id", id);
	    if (priority != null) {
	        countQuery.setParameter("priority", priority);
	    }
	    if (status != null) {
	        countQuery.setParameter("status", status);
	    }
	    long totalElements = ((Number) countQuery.getSingleResult()).longValue();

	    return new PageImpl<>(taskList, pageable, totalElements);
	}

	
	
	@Override
	public Page<Task> findTasksForCurrentAssignee(Long id, TaskPriority priority, TaskStatus status, Pageable pageable) {
		
		StringBuilder queryStr = new StringBuilder("SELECT t FROM Task t JOIN t.performers p WHERE p.id = :user_id");
		if (priority != null) {
	        queryStr.append(" AND t.priority = :priorityStr");
	    }

	    if (status != null) {
	        queryStr.append(" AND t.status = :statusStr");
	    }
	    
	    if (pageable.getSort() != null && pageable.getSort().isSorted()) {
	        queryStr.append(" ORDER BY ");
	        
	        pageable.getSort().forEach(order -> {
	            queryStr.append("t." + order.getProperty() + " " + (order.isAscending() ? "ASC" : "DESC") + ", ");
	        });

	        queryStr.delete(queryStr.length() - 2, queryStr.length());
	    }
	    
	    
	    
	    Query query = entityManager.createQuery(queryStr.toString(), Task.class);
	    
	    query.setParameter("user_id", id);
	    if (priority != null) {
	        query.setParameter("priorityStr", priority);
	    }
	    if (status != null) {
	        query.setParameter("statusStr", status);
	    }
	    query.setFirstResult((int) pageable.getOffset());
	    
	    query.setMaxResults(pageable.getPageSize());
	    StringBuilder countQueryStr = new StringBuilder("SELECT COUNT(t) FROM Task t JOIN t.performers p WHERE p.id = :user_id");
	    if (priority != null) {
	        countQueryStr.append(" AND t.priority = :priorityStr");
	    }
	    if (status != null) {
	        countQueryStr.append(" AND t.status = :statusStr");
	    }
	    Query countQuery = entityManager.createQuery(countQueryStr.toString());
	    
	    countQuery.setParameter("user_id", id);
	    if (priority != null) {
	        countQuery.setParameter("priorityStr", priority);
	    }
	    if (status != null) {
	        countQuery.setParameter("statusStr", status);
	    }
	    long totalElements = ((Number) countQuery.getSingleResult()).longValue();
	    
	    List<Task> taskList = query.getResultList();
	    
	    Page<Task> tasks = new PageImpl<>(taskList, pageable, totalElements);
	    
	    return tasks;
    }
	
	@Override
	public Page<Task> findByPriorityAndStatus(TaskPriority priority, TaskStatus status, Pageable pageable) {
		return null;
	}
	
	@Override
	public Task updateTask(Task task) {
		Session session = entityManager.unwrap(Session.class);
		session.saveOrUpdate(task);
		return task;
	}
	
	public boolean isPerformer(Long user_id, Task task) {
		String query = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM performers WHERE user_id = :userId AND task_id = :taskId";
		
		Boolean exists = (Boolean) entityManager.createNativeQuery(query)
		    .setParameter("userId", user_id)
		    .setParameter("taskId", task.getId())
		    .getSingleResult();

		
		return exists;
	}

}
