package com.sumedh.userservice.service;

import com.sumedh.userservice.dto.LoginRequestDTO;
import com.sumedh.userservice.dto.RegisterRequestDTO;

public interface AuthService {
    void registerUser(RegisterRequestDTO request);

    boolean login(LoginRequestDTO request);

    String getRoleByEmail(String email);

    Long getUserIdByEmail(String email);
}