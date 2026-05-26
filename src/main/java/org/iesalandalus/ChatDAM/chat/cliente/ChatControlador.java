package org.iesalandalus.ChatDAM.chat.cliente;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.iesalandalus.ChatDAM.model.Mensaje;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ChatControlador {

    @FXML private MenuItem menuSalir;
    @FXML private ScrollPane scrollMensajes;
    @FXML private VBox vboxMensajes;
    @FXML private TextField txtInputMensaje;
    @FXML private Button btnEnviar;

    private ChatClienteService clienteService;
    private String miNombreUsuario;
    private Timeline actualizadorAutomatico;

    @FXML
    public void initialize() {
        clienteService = new ChatClienteService();


        actualizarMensajes();

        actualizadorAutomatico = new Timeline(new KeyFrame(Duration.seconds(3), evento -> {
            actualizarMensajes();
        }));
        actualizadorAutomatico.setCycleCount(Animation.INDEFINITE);
        actualizadorAutomatico.play();

        vboxMensajes.heightProperty().addListener((observable, oldVal, newVal) -> {
            scrollMensajes.setVvalue(1.0);
        });
    }

    // Nuevo método para inyectar el nombre desde el Login
    public void setNombreUsuario(String nombre) {
        this.miNombreUsuario = nombre;
    }


    private void actualizarMensajes() {
        List<Mensaje> ultimosMensajes = clienteService.obtenerUltimosMensajes();
        Platform.runLater(() -> {
            vboxMensajes.getChildren().clear();

            for (Mensaje m : ultimosMensajes) {
                HBox contenedorBurbuja = crearBurbuja(m);
                vboxMensajes.getChildren().add(contenedorBurbuja);
            }
        });
    }

    private HBox crearBurbuja(Mensaje mensaje) {
        VBox burbuja = new VBox();
        burbuja.setSpacing(5);

        Label lblNombre = new Label(mensaje.getNombreUsuario());
        lblNombre.getStyleClass().add("texto-nombre");
        Label lblTexto = new Label(mensaje.getContenido());
        lblTexto.getStyleClass().add("texto-mensaje");
        lblTexto.setWrapText(true);
        lblTexto.setMaxWidth(300);

        Label lblHora = new Label(mensaje.getFecha() + " a las " + mensaje.getHora());
        lblHora.getStyleClass().add("texto-hora");

        burbuja.getChildren().addAll(lblNombre, lblTexto, lblHora);

        HBox alineacion = new HBox();

        if (mensaje.getNombreUsuario().equalsIgnoreCase(miNombreUsuario)) {
            burbuja.getStyleClass().add("burbuja-mia");
            alineacion.setAlignment(Pos.CENTER_RIGHT);
        } else {
            burbuja.getStyleClass().add("burbuja-otro");
            alineacion.setAlignment(Pos.CENTER_LEFT);
        }

        alineacion.getChildren().add(burbuja);
        return alineacion;
    }

    @FXML
    void enviarMensaje(ActionEvent event) {
        String texto = txtInputMensaje.getText();

        if (texto != null && !texto.trim().isEmpty()) {
            String horaActual = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            LocalDate fechaActual = LocalDate.now();

            Mensaje nuevoMensaje = new Mensaje(texto, miNombreUsuario, fechaActual, horaActual);

            clienteService.enviarNuevoMensaje(nuevoMensaje);

            txtInputMensaje.clear();
            actualizarMensajes();
        }
    }

    @FXML
    void salirAplicacion(ActionEvent event) {
        System.exit(0);
    }
}