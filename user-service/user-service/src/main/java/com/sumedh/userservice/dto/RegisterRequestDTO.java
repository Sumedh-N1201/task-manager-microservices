package com.sumedh.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Email(message = "Invalid email")
    private String email;

    @Size(min = 6, message = "Password must have at least 6 characters")
    private String password;

    @NotBlank(message = "Please state your role")
    private String role;
}