package org.iesalandalus.ChatDAM.chat.cliente;

import org.iesalandalus.ChatDAM.model.Mensaje;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

public class ChatClienteService {

    private final String URL_GET = "http://localhost:8080/api/chat/mensajes";
    private final String URL_POST = "http://localhost:8080/api/chat/mensaje";

    private final RestTemplate restTemplate;

    public ChatClienteService() {
        this.restTemplate = new RestTemplate();
    }

    public List<Mensaje> obtenerUltimosMensajes() {
        try {
            Mensaje[] mensajesArray = restTemplate.getForObject(URL_GET, Mensaje[].class);
            if (mensajesArray != null) {
                return Arrays.asList(mensajesArray);
            }
        } catch (Exception e) {
            System.err.println("Seguridad cliente - Error de conexión al descargar mensajes: " + e.getMessage());
        }
        return List.of();
    }

    public void enviarNuevoMensaje(Mensaje nuevoMensaje) {
        if (nuevoMensaje == null) {
            System.err.println("Seguridad cliente - Intento de enviar un objeto mensaje nulo cancelado.");
            return;
        }

        try {
            restTemplate.postForObject(URL_POST, nuevoMensaje, Void.class);
        } catch (Exception e) {
            System.err.println("Seguridad cliente - La API rechazó el mensaje (Motivo: Datos inválidos o servidor caído): " + e.getMessage());
        }
    }
}
