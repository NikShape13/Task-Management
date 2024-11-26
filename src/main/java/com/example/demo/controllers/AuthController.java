package com.example.demo.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.models.JwtAuthenticationResponse;
import com.example.demo.services.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Management", description = "Operations related to sign in and sign up")
public class AuthController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "User registration", description = "Creates new users with role 'USER' and gives jwt token")
    @PostMapping("/sign_up")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest request) {
    	try {
	        return ResponseEntity.ok(authenticationService.signUp(request));
	    } catch (Exception e) {
			Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("info", e.getMessage());
	    	return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
		}
    }
    
    @Operation(summary = "Admin registration (You will need the admin role to test most features)", description = "Creates new users with role 'ADMIN' and gives jwt token")
    @PostMapping("/sign_up_for_admins")
    public ResponseEntity<?> signUpForAdmins(@RequestBody @Valid SignUpRequest request) {
    	try {
	        return ResponseEntity.ok(authenticationService.signUpForAdmins(request));
	    } catch (Exception e) {
			Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("info", e.getMessage());
	    	return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
		}
    }

    @Operation(summary = "User authorization", description = "Gives a jwt token for users")
    @PostMapping("/sign_in")
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInRequest request) {
        try {
			return ResponseEntity.ok(authenticationService.signIn(request));
		} catch (Exception e) {
			Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("info", e.getMessage());
	    	return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
		}
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errorMap.put(fieldName, message);
        });
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }
    
}