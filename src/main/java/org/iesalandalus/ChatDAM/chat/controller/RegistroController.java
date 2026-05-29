package org.iesalandalus.ChatDAM.chat.controller;

import org.iesalandalus.ChatDAM.chat.dto.RegistroRequest;
import org.iesalandalus.ChatDAM.chat.dto.RegistroResponse;
import org.iesalandalus.ChatDAM.chat.service.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class RegistroController {

    @Autowired
    private RegistroService registroService;

    @PostMapping("/registro")
    public ResponseEntity<RegistroResponse> registro(@RequestBody RegistroRequest request) {
        RegistroResponse response = registroService.registrarUsuario(request);
        return ResponseEntity.ok(response);
    }
}