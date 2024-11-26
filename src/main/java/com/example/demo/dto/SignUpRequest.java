package com.example.demo.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Registration request")
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @Schema(description = "User name", example = "Jonn Deer")
    @Size(min = 5, max = 50, message = "User name should be at least 5 and less than 50 symbols")
    @NotBlank(message = "User name can not be blank")
    private String name;

    @Schema(description = "Email", example = "jondoe@gmail.com")
    @Size(min = 5, max = 50, message = "Email should be at least 5 and less than 50 symbols")
    @NotBlank(message = "Email can not be blank")
    @Email(message = "Email should be in this format: user@example.com")
    private String email;

    @Schema(description = "Password", example = "my_1secret1_password")
    @Size(min = 5, max = 50, message = "Password should be at least 5 and less than 50 symbols")
    @NotBlank(message = "Password can not be blank")
    private String password;

}