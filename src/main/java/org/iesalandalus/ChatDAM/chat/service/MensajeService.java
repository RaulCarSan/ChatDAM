package org.iesalandalus.ChatDAM.chat.service;

import org.iesalandalus.ChatDAM.chat.repository.MensajeRepository;
import org.iesalandalus.ChatDAM.chat.repository.UsuarioRepository;
import org.iesalandalus.ChatDAM.model.Mensaje;
import org.iesalandalus.ChatDAM.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MensajeService {

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; // NUEVO

    public List<Mensaje> obtenerUltimos10Mensajes() {
        List<Mensaje> todos = mensajeRepository.findAll();
        if (todos.size() > 10) {
            return todos.subList(todos.size() - 10, todos.size());
        }
        return todos;
    }

    public void guardarMensaje(Mensaje mensaje) {
        if (mensaje == null) {
            throw new IllegalArgumentException("No se puede guardar un mensaje nulo");
        }
        mensajeRepository.save(mensaje);
    }

    public boolean borrarMensaje(Long idMensaje, String nombreSolicitante) {
        Optional<Mensaje> mensajeOpt = mensajeRepository.findById(idMensaje);
        if (mensajeOpt.isEmpty()) return false;

        Optional<Usuario> usuarioOpt = usuarioRepository.findByNombreUsuario(nombreSolicitante);
        if (usuarioOpt.isEmpty()) return false;

        Mensaje mensaje  = mensajeOpt.get();
        Usuario solicitante = usuarioOpt.get();

        boolean esAdmin    = "ADMINISTRADOR".equals(solicitante.getRol());
        boolean esPropietario = mensaje.getNombreUsuario().equalsIgnoreCase(nombreSolicitante);

        if (esAdmin || esPropietario) {
            mensajeRepository.deleteById(idMensaje);
            return true;
        }
        return false;
    }
}