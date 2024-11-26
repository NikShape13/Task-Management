package com.example.demo.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "tokens")
public class Token {

	@Id
	@Column(name="user_id")
	private long user_id;
	@Column(name="token")
	private String token;
	
	
	public Token(long user_id, String token) {
		this.user_id = user_id;
		this.token = token;
	}
}
