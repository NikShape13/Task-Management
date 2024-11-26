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

import com.example.demo.models.Role;
import com.example.demo.models.User;

import jakarta.persistence.EntityManager;

@Repository
public class RoleRepositoryImpl implements RoleRepository{
	@Autowired
	private EntityManager entityManager;

	@Override
	public void flush() {
		
	}

	@Override
	public <S extends Role> S saveAndFlush(S entity) {
		return null;
	}

	@Override
	public <S extends Role> List<S> saveAllAndFlush(Iterable<S> entities) {
		return null;
	}

	@Override
	public void deleteAllInBatch(Iterable<Role> entities) {
		
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Long> ids) {
		
	}

	@Override
	public void deleteAllInBatch() {
		
	}

	@Override
	public Role getOne(Long id) {
		return null;
	}

	@Override
	public Role getById(Long id) {
		return null;
	}

	@Override
	public Role getReferenceById(Long id) {
		return null;
	}

	@Override
	public <S extends Role> List<S> findAll(Example<S> example) {
		return null;
	}

	@Override
	public <S extends Role> List<S> findAll(Example<S> example, Sort sort) {
		return null;
	}

	@Override
	public <S extends Role> List<S> saveAll(Iterable<S> entities) {
		return null;
	}

	@Override
	public List<Role> findAll() {
		return null;
	}

	@Override
	public List<Role> findAllById(Iterable<Long> ids) {
		return null;
	}

	@Override
	public <S extends Role> S save(S entity) {
		Session session = entityManager.unwrap(Session.class);
		session.saveOrUpdate(entity);
		return entity;
	}

	@Override
	public Optional<Role> findById(Long id) {
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
	public void delete(Role entity) {
		
	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {
		
	}

	@Override
	public void deleteAll(Iterable<? extends Role> entities) {
		
	}

	@Override
	public void deleteAll() {
		
	}

	@Override
	public List<Role> findAll(Sort sort) {
		return null;
	}

	@Override
	public Page<Role> findAll(Pageable pageable) {
		return null;
	}

	@Override
	public <S extends Role> Optional<S> findOne(Example<S> example) {
		return Optional.empty();
	}

	@Override
	public <S extends Role> Page<S> findAll(Example<S> example, Pageable pageable) {
		return null;
	}

	@Override
	public <S extends Role> long count(Example<S> example) {
		return 0;
	}

	@Override
	public <S extends Role> boolean exists(Example<S> example) {
		return false;
	}

	@Override
	public <S extends Role, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		return null;
	}

	@Override
	public Optional<Role> findByName(String name) {
		Session session = entityManager.unwrap(Session.class);
	    Role role = session.createQuery("FROM Role WHERE name = :name", Role.class)
	                       .setParameter("name", name)
	                       .uniqueResult();
		return Optional.of(role);
	}
	
	
	
	

}
