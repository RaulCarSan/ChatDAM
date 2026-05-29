package org.iesalandalus.ChatDAM.chat.controller;

import jakarta.validation.Valid;
import org.iesalandalus.ChatDAM.chat.service.MensajeService;
import org.iesalandalus.ChatDAM.model.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class MensajeController {

    @Autowired
    private MensajeService mensajeService;

    @GetMapping("/mensajes")
    public List<Mensaje> obtenerMensajes() {
        return mensajeService.obtenerUltimos10Mensajes();
    }

    @PostMapping("/mensaje")
    public void enviarMensaje(@Valid @RequestBody Mensaje mensaje) {
        mensajeService.guardarMensaje(mensaje);
    }

    @DeleteMapping("/mensaje/{id}")
    public ResponseEntity<String> borrarMensaje(
            @PathVariable Long id,
            @RequestHeader("X-Usuario") String nombreSolicitante) {

        boolean borrado = mensajeService.borrarMensaje(id, nombreSolicitante);

        if (borrado) {
            return ResponseEntity.ok("Mensaje eliminado");
        } else {
            return ResponseEntity.status(403).body("Sin permiso o mensaje no encontrado");
        }
    }
}