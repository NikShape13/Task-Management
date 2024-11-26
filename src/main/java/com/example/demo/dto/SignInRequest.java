package com.example.demo.dto;

import com.example.demo.services.JwtService;
import com.example.demo.services.UserService;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Schema(description = "Authentication request")
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {

    @Schema(description = "Email", example = "jondoe@gmail.com")
    @Size(min = 5, max = 50, message = "Email should be at least 5 and less than 50 symbols")
    @NotBlank(message = "Email can not be blank")
    private String email;

    @Schema(description = "Password", example = "my_1secret1_password")
    @Size(min = 5, max = 50, message = "Password should be at least 5 and less than 50 symbols")
    @NotBlank(message = "Password can not be blank")
    private String password;
}