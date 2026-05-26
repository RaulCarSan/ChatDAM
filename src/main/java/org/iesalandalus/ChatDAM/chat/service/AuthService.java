package org.iesalandalus.ChatDAM.chat.service;

import org.iesalandalus.ChatDAM.chat.dto.LoginRequest;
import org.iesalandalus.ChatDAM.chat.dto.LoginResponse;
import org.iesalandalus.ChatDAM.chat.repository.UsuarioRepository;
import org.iesalandalus.ChatDAM.model.Usuario;
import org.iesalandalus.ChatDAM.util.CifradoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public LoginResponse validarLogin(LoginRequest request) {
        try {
            // Descifrar credenciales recibidas de JavaFX
            String nombreUsuario = CifradoUtil.descifrar(request.getNombreUsuarioCifrado());
            String password      = CifradoUtil.descifrar(request.getPasswordCifrado());

            // Nunca logueamos contraseñas
            System.out.println("Intento de login para usuario: " + nombreUsuario);

            // Buscar usuario en BD
            Optional<Usuario> usuarioOpt = usuarioRepository.findByNombreUsuario(nombreUsuario);

            if (usuarioOpt.isEmpty()) {
                return new LoginResponse(false, "Usuario no encontrado", null);
            }

            // Comparar hash SHA-256 de la contraseña recibida con el hash almacenado
            String hashRecibido = CifradoUtil.hashSHA256(password);
            Usuario usuario = usuarioOpt.get();

            if (!hashRecibido.equals(usuario.getPasswordHash())) {
                return new LoginResponse(false, "Contraseña incorrecta", null);
            }

            return new LoginResponse(true, "Login correcto", usuario.getNombreUsuario());

        } catch (Exception e) {
            // No revelar detalles del error al cliente
            System.err.println("Error en validación de login: " + e.getMessage());
            return new LoginResponse(false, "Error interno de autenticación", null);
        }
    }
}