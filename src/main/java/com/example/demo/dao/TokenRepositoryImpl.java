package com.example.demo.dao;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Token;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Repository
public class TokenRepositoryImpl implements TokenRepository{
	@Autowired
	private EntityManager entityManager;

	@Override
	@Transactional
	public Token getById(int user_id) {
		Session session = entityManager.unwrap(Session.class);
		return session.createQuery("FROM Token WHERE user_id = :user_id", Token.class)
                .setParameter("user_id", user_id)
                .uniqueResult();
	}

	@Override
	@Transactional
	public Token saveToken(Token token) {
		Session session = entityManager.unwrap(Session.class);
		session.saveOrUpdate(token);
		return token;
	}

}
