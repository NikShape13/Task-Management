package com.example.demo.services;

import org.springframework.stereotype.Service;

import com.example.demo.dao.TokenRepository;
import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.models.JwtAuthenticationResponse;
import com.example.demo.models.PasswordEncoder;
import com.example.demo.models.Token;
import com.example.demo.models.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    
    public JwtAuthenticationResponse signUp(SignUpRequest request) throws Exception {
    	User user = userService.createUser(request);

        var jwt = jwtService.generateToken(user);
        tokenRepository.saveToken(new Token(user.getId(), jwt));
        return new JwtAuthenticationResponse(jwt);
    }
    
    public JwtAuthenticationResponse signUpForAdmins(SignUpRequest request) throws Exception {
    	User user = userService.createAdmin(request);

        var jwt = jwtService.generateToken(user);
        tokenRepository.saveToken(new Token(user.getId(), jwt));
        return new JwtAuthenticationResponse(jwt);
    }

    
    public JwtAuthenticationResponse signIn(SignInRequest request) throws Exception {
    	User user = userService.getByEmail(request.getEmail());
        if (user == null) {
            throw new Exception("User not found");
        }
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new Exception("Invalid password");
        }
        var jwt = jwtService.generateToken(user);
        tokenRepository.saveToken(new Token(user.getId(), jwt));
        return new JwtAuthenticationResponse(jwt);
    }
}