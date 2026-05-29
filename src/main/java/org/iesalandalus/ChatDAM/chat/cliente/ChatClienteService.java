package org.iesalandalus.ChatDAM.chat.cliente;

import org.iesalandalus.ChatDAM.model.Mensaje;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class ChatClienteService {

    private final String URL_GET    = "http://localhost:8080/api/chat/mensajes";
    private final String URL_POST   = "http://localhost:8080/api/chat/mensaje";
    private final String URL_DELETE = "http://localhost:8080/api/chat/mensaje/";

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Mensaje> obtenerUltimosMensajes() {
        try {
            Mensaje[] array = restTemplate.getForObject(URL_GET, Mensaje[].class);
            return array != null ? Arrays.asList(array) : List.of();
        } catch (Exception e) {
            System.err.println("Error al descargar mensajes: " + e.getMessage());
            return List.of();
        }
    }

    public void enviarNuevoMensaje(Mensaje nuevoMensaje) {
        if (nuevoMensaje == null) return;
        try {
            restTemplate.postForObject(URL_POST, nuevoMensaje, Void.class);
        } catch (Exception e) {
            System.err.println("Error al enviar mensaje: " + e.getMessage());
        }
    }

    public boolean borrarMensaje(Long idMensaje, String nombreUsuario) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Usuario", nombreUsuario);
            HttpEntity<Void> peticion = new HttpEntity<>(headers);

            ResponseEntity<String> respuesta = restTemplate.exchange(
                    URL_DELETE + idMensaje,
                    HttpMethod.DELETE,
                    peticion,
                    String.class
            );
            return respuesta.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            System.err.println("Error al borrar mensaje: " + e.getMessage());
            return false;
        }
    }
}