package org.iesalandalus.ChatDAM.chat.service;

import org.iesalandalus.ChatDAM.chat.dto.RegistroRequest;
import org.iesalandalus.ChatDAM.chat.dto.RegistroResponse;
import org.iesalandalus.ChatDAM.chat.repository.UsuarioRepository;
import org.iesalandalus.ChatDAM.model.Usuario;
import org.iesalandalus.ChatDAM.util.CifradoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistroService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public RegistroResponse registrarUsuario(RegistroRequest request) {
        try {
            String nombreUsuario = CifradoUtil.descifrar(request.getNombreUsuarioCifrado());
            String password      = CifradoUtil.descifrar(request.getPasswordCifrado());

            System.out.println("Intento de registro para usuario: " + nombreUsuario);
            if (!nombreUsuario.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
                return new RegistroResponse(false, "El nombre de usuario solo puede contener letras.");
            }

            if (password.length() < 6) {
                return new RegistroResponse(false, "La contraseña debe tener al menos 6 caracteres.");
            }
            if (usuarioRepository.findByNombreUsuario(nombreUsuario).isPresent()) {
                return new RegistroResponse(false, "El nombre de usuario ya está en uso.");
            }

            String passwordHash = CifradoUtil.hashSHA256(password);

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombreUsuario(nombreUsuario);
            nuevoUsuario.setPasswordHash(passwordHash);
            nuevoUsuario.setRol("USUARIO");
            usuarioRepository.save(nuevoUsuario);

            System.out.println("Usuario registrado correctamente: " + nombreUsuario);
            return new RegistroResponse(true, "Registro completado. Ya puedes iniciar sesión.");

        } catch (Exception e) {
            System.err.println("Error en el registro: " + e.getMessage());
            return new RegistroResponse(false, "Error interno al procesar el registro.");
        }
    }
}