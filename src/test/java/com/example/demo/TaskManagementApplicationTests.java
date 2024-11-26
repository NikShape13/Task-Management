package com.example.demo;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.models.JwtAuthenticationResponse;
import com.example.demo.services.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class TaskManagementApplicationTests {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testSignUp_Success() throws Exception {
        SignUpRequest request = new SignUpRequest("adminUser", "admin@example.com", "adminPass123");
        when(authenticationService.signUp(request))
            .thenReturn(new JwtAuthenticationResponse("jwt-token"));

        mockMvc.perform(post("/auth/sign_up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("jwt-token"));
    }
    
    @Test
    void testSignUp_Failure() throws Exception {
        SignUpRequest request = new SignUpRequest("adminUser", "admin@example.com", "adminPass123");
        when(authenticationService.signUp(request))
            .thenThrow(new RuntimeException("User already exists"));

        mockMvc.perform(post("/auth/sign_up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.info").value("User already exists"));
    }
    

    @Test
    void testSignIn_Success() throws Exception {
        SignInRequest request = new SignInRequest("test@example.com", "password123");
        when(authenticationService.signIn(request))
            .thenReturn(new JwtAuthenticationResponse("jwt-token"));

        mockMvc.perform(post("/auth/sign_in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    void testSignUpForAdmins_Success() throws Exception {
        SignUpRequest request = new SignUpRequest("adminUser", "admin@example.com", "adminPass123");
        when(authenticationService.signUpForAdmins(request))
            .thenReturn(new JwtAuthenticationResponse("jwt-token"));

        mockMvc.perform(post("/auth/sign_up_for_admins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("jwt-token"));
    }

}
