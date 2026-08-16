package com.sumedh.userservice.controller;

import com.sumedh.userservice.dto.AuthResponseDTO;
import com.sumedh.userservice.dto.LoginRequestDTO;
import com.sumedh.userservice.dto.RegisterRequestDTO;
import com.sumedh.userservice.security.JwtUtil;
import com.sumedh.userservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDTO request) {
        authService.registerUser(request);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        boolean isValid = authService.login(request);

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials !");
        }

        Long userId = authService.getUserIdByEmail(request.getEmail());
        String role = authService.getRoleByEmail(request.getEmail());
        String token = jwtUtil.generateToken(userId, request.getEmail(), role);

        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}