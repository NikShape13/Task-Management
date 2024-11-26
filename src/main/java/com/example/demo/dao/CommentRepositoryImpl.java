package com.example.demo.dao;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Comment;

import jakarta.persistence.EntityManager;

@Repository
public class CommentRepositoryImpl implements CommentRepository {
	@Autowired
	private EntityManager entityManager;

	@Override
	public void flush() {
		
	}

	@Override
	public <S extends Comment> S saveAndFlush(S entity) {
		return null;
	}

	@Override
	public <S extends Comment> List<S> saveAllAndFlush(Iterable<S> entities) {
		return null;
	}

	@Override
	public void deleteAllInBatch(Iterable<Comment> entities) {
		
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Long> ids) {
		
	}

	@Override
	public void deleteAllInBatch() {
		
	}

	@Override
	public Comment getOne(Long id) {
		return null;
	}

	@Override
	public Comment getById(Long id) {
		return null;
	}

	@Override
	public Comment getReferenceById(Long id) {
		return null;
	}

	@Override
	public <S extends Comment> List<S> findAll(Example<S> example) {
		return null;
	}

	@Override
	public <S extends Comment> List<S> findAll(Example<S> example, Sort sort) {
		return null;
	}

	@Override
	public <S extends Comment> List<S> saveAll(Iterable<S> entities) {
		return null;
	}

	@Override
	public List<Comment> findAll() {
		return null;
	}

	@Override
	public List<Comment> findAllById(Iterable<Long> ids) {
		return null;
	}

	@Override
	public <S extends Comment> S save(S entity) {
		Session session = entityManager.unwrap(Session.class);
		session.saveOrUpdate(entity);
		return entity;
	}

	@Override
	public Optional<Comment> findById(Long id) {
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
	public void delete(Comment entity) {
		
	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {
		
	}

	@Override
	public void deleteAll(Iterable<? extends Comment> entities) {
		
	}

	@Override
	public void deleteAll() {
		
	}

	@Override
	public List<Comment> findAll(Sort sort) {
		return null;
	}

	@Override
	public Page<Comment> findAll(Pageable pageable) {
		return null;
	}

	@Override
	public <S extends Comment> Optional<S> findOne(Example<S> example) {
		return Optional.empty();
	}

	@Override
	public <S extends Comment> Page<S> findAll(Example<S> example, Pageable pageable) {
		return null;
	}

	@Override
	public <S extends Comment> long count(Example<S> example) {
		return 0;
	}

	@Override
	public <S extends Comment> boolean exists(Example<S> example) {
		return false;
	}

	@Override
	public <S extends Comment, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		return null;
	}

	@Override
	public List<Comment> findByTaskId(Long task_id) {
		return null;
	}

	@Override
	public List<Comment> findByUserId(Long user_id) {
		return null;
	}

	@Override
	public List<Comment> findByTaskIdAndUserId(Long task_id, Long user_id) {
		return null;
	}

}
