package org.iesalandalus.ChatDAM.chat.cliente;

import org.iesalandalus.ChatDAM.chat.dto.LoginRequest;
import org.iesalandalus.ChatDAM.chat.dto.LoginResponse;
import org.iesalandalus.ChatDAM.util.CifradoUtil;
import org.springframework.web.client.RestTemplate;

public class AuthClienteService {

    private static final String URL_LOGIN = "http://localhost:8080/api/auth/login";
    private final RestTemplate restTemplate = new RestTemplate();

    public LoginResponse login(String usuario, String password) {
        try {
            // Cifrar credenciales antes de enviarlas
            String usuarioCifrado  = CifradoUtil.cifrar(usuario);
            String passwordCifrado = CifradoUtil.cifrar(password);

            LoginRequest request = new LoginRequest();
            request.setNombreUsuarioCifrado(usuarioCifrado);
            request.setPasswordCifrado(passwordCifrado);

            return restTemplate.postForObject(URL_LOGIN, request, LoginResponse.class);

        } catch (Exception e) {
            System.err.println("Seguridad cliente - Error en login: " + e.getMessage());
            return null;
        }
    }
}