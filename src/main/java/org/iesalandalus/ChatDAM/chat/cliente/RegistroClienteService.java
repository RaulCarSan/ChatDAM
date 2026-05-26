package org.iesalandalus.ChatDAM.chat.cliente;

import org.iesalandalus.ChatDAM.chat.dto.RegistroRequest;
import org.iesalandalus.ChatDAM.chat.dto.RegistroResponse;
import org.iesalandalus.ChatDAM.util.CifradoUtil;
import org.springframework.web.client.RestTemplate;

public class RegistroClienteService {

    private static final String URL_REGISTRO = "http://localhost:8080/api/auth/registro";
    private final RestTemplate restTemplate = new RestTemplate();

    public RegistroResponse registrar(String usuario, String password) {
        try {
            // Cifrar credenciales antes de enviarlas (AES, igual que en el login)
            String usuarioCifrado  = CifradoUtil.cifrar(usuario);
            String passwordCifrado = CifradoUtil.cifrar(password);

            RegistroRequest request = new RegistroRequest();
            request.setNombreUsuarioCifrado(usuarioCifrado);
            request.setPasswordCifrado(passwordCifrado);

            return restTemplate.postForObject(URL_REGISTRO, request, RegistroResponse.class);

        } catch (Exception e) {
            System.err.println("Seguridad cliente - Error en registro: " + e.getMessage());
            return null;
        }
    }
}