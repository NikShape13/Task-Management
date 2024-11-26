package com.example.demo.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.demo.models.Role;
import com.example.demo.models.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    
    public String extractEmail(String token) {
    	return extractClaim(token, claims -> claims.get("email", String.class));
    }
    
    public List<String> extractRoles(String token) {
    	return extractClaim(token, claims -> {
            List<?> roles = (List<?>) claims.get("role"); 
            return roles.stream()
                        .map(role -> ((Map<String, Object>) role).get("name").toString()) 
                        .collect(Collectors.toList());
        });
    }

    
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        if (user instanceof User) {
        	User customUserDetails = (User) user;
            claims.put("id", customUserDetails.getId());
            claims.put("email", customUserDetails.getEmail());
            claims.put("role", customUserDetails.getRoles());
        }
        return generateToken(claims, user);
    }

    
    public boolean isTokenValid(String token, User user) {
        final String email = extractEmail(token);
        return (email.equals(user.getEmail())) && !isTokenExpired(token);
    }

    
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
    	
    	token = token.startsWith("Bearer ") ? token.substring(7).trim() : token.trim();
        Claims claims = null;
		
		claims = extractAllClaims(token);
		
        
        return claimsResolvers.apply(claims);
    }
    
    public Long extractId(String token) {
    	return extractClaim(token, claims -> claims.get("id", Long.class));
    }

    
    private String generateToken(Map<String, Object> extraClaims, User user) {
        return Jwts.builder().setClaims(extraClaims).setSubject(user.getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    
    private Claims extractAllClaims(String token) {
        
            return Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        
        
    }

    
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
