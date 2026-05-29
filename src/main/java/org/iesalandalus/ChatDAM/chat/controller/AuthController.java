package org.iesalandalus.ChatDAM.chat.controller;

import org.iesalandalus.ChatDAM.chat.dto.LoginRequest;
import org.iesalandalus.ChatDAM.chat.dto.LoginResponse;
import org.iesalandalus.ChatDAM.chat.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.validarLogin(request);
        return ResponseEntity.ok(response);
    }
}