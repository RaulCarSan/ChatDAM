package org.iesalandalus.ChatDAM.javafx;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.iesalandalus.ChatDAM.chat.cliente.ChatClienteService;
import org.iesalandalus.ChatDAM.model.Mensaje;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatVentana {

    // ← Cambia "ApellidosNombre" por los tuyos reales
    private static final String TITULO_VENTANA = "ChatDAM_ApellidosNombreAlumno";

    private final String nombreUsuario;
    private final ChatClienteService chatService = new ChatClienteService();
    private TextArea areaChat;

    public ChatVentana(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void mostrar(Stage stage) {
        stage.setTitle(TITULO_VENTANA);

        // Área de mensajes (solo lectura)
        areaChat = new TextArea();
        areaChat.setEditable(false);
        areaChat.setWrapText(true);
        VBox.setVgrow(areaChat, Priority.ALWAYS);

        // Campo de escritura
        TextField tfMensaje = new TextField();
        tfMensaje.setPromptText("Escribe tu mensaje...");

        Button btnEnviar    = new Button("Enviar");
        Button btnRefrescar = new Button("↻ Actualizar");

        btnEnviar.setDefaultButton(true);
        btnEnviar.setOnAction(e -> {
            String texto = tfMensaje.getText().trim();
            if (!texto.isEmpty()) {
                enviarMensaje(texto);
                tfMensaje.clear();
            }
        });

        btnRefrescar.setOnAction(e -> cargarMensajes());

        HBox barraEnvio = new HBox(8, tfMensaje, btnEnviar, btnRefrescar);
        HBox.setHgrow(tfMensaje, Priority.ALWAYS);
        barraEnvio.setPadding(new Insets(5));

        Label lblUsuario = new Label("Conectado como: " + nombreUsuario);
        lblUsuario.setStyle("-fx-font-style: italic; -fx-text-fill: gray;");

        VBox layout = new VBox(8, lblUsuario, areaChat, barraEnvio);
        layout.setPadding(new Insets(10));

        stage.setScene(new Scene(layout, 600, 450));
        stage.show();

        // Cargar mensajes al abrir
        cargarMensajes();
    }

    private void cargarMensajes() {
        new Thread(() -> {
            List<Mensaje> mensajes = chatService.obtenerUltimosMensajes();
            StringBuilder sb = new StringBuilder();
            for (Mensaje m : mensajes) {
                sb.append("[").append(m.getFecha()).append(" ").append(m.getHora()).append("] ")
                        .append(m.getNombreUsuario()).append(": ")
                        .append(m.getContenido()).append("\n");
            }
            javafx.application.Platform.runLater(() -> areaChat.setText(sb.toString()));
        }).start();
    }

    private void enviarMensaje(String texto) {
        String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        Mensaje nuevo = new Mensaje(texto, nombreUsuario, LocalDate.now(), hora);
        new Thread(() -> chatService.enviarNuevoMensaje(nuevo)).start();
    }
}