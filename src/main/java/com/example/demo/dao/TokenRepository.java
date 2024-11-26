package com.example.demo.dao;

import com.example.demo.models.Token;


public interface TokenRepository {
	public Token getById(int user_id);
	
	public Token saveToken(Token token);
}
