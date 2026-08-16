package com.sumedh.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @Email(message = "Please enter a valid mail ID")
    private String email;

    @Size(min = 6, message = "Password should at least contain 6 characters")
    private String password;
}