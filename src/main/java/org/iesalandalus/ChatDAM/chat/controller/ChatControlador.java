package org.iesalandalus.ChatDAM.chat.controller;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import org.iesalandalus.ChatDAM.chat.cliente.ChatClienteService;
import org.iesalandalus.ChatDAM.model.Mensaje;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatControlador {

    @FXML private MenuItem menuSalir;
    @FXML private ScrollPane scrollMensajes;
    @FXML private VBox vboxMensajes;
    @FXML private TextField txtInputMensaje;
    @FXML private Button btnEnviar;

    private ChatClienteService clienteService;
    private String miNombreUsuario;
    private String miRol; // NUEVO
    private Timeline actualizadorAutomatico;

    @FXML
    public void initialize() {
        clienteService = new ChatClienteService();
        vboxMensajes.heightProperty().addListener((obs, oldVal, newVal) ->
                scrollMensajes.setVvalue(1.0));
    }

    public void setNombreUsuario(String nombre) {
        setNombreUsuario(nombre, "USUARIO");
    }

    public void setNombreUsuario(String nombre, String rol) {
        this.miNombreUsuario = nombre;
        this.miRol = rol;
        System.out.println("Usuario: " + miNombreUsuario + " | Rol: " + miRol);

        actualizarMensajes();

        actualizadorAutomatico = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> actualizarMensajes()));
        actualizadorAutomatico.setCycleCount(Animation.INDEFINITE);
        actualizadorAutomatico.play();
    }

    private void actualizarMensajes() {
        List<Mensaje> mensajes = clienteService.obtenerUltimosMensajes();
        Platform.runLater(() -> {
            vboxMensajes.getChildren().clear();
            for (Mensaje m : mensajes) {
                vboxMensajes.getChildren().add(crearBurbuja(m));
            }
        });
    }

    private HBox crearBurbuja(Mensaje mensaje) {
        VBox burbuja = new VBox(5);

        Label lblNombre = new Label(mensaje.getNombreUsuario());
        lblNombre.getStyleClass().add("texto-nombre");

        Label lblTexto = new Label(mensaje.getContenido());
        lblTexto.getStyleClass().add("texto-mensaje");
        lblTexto.setWrapText(true);
        lblTexto.setMaxWidth(280);

        Label lblHora = new Label(mensaje.getFecha() + " a las " + mensaje.getHora());
        lblHora.getStyleClass().add("texto-hora");

        boolean esAdmin       = "ADMINISTRADOR".equals(miRol);
        boolean esPropietario = miNombreUsuario != null &&
                mensaje.getNombreUsuario().equalsIgnoreCase(miNombreUsuario);

        HBox filaInferior = new HBox(5);
        filaInferior.setAlignment(Pos.CENTER_RIGHT);
        filaInferior.getChildren().add(lblHora);

        if (esAdmin || esPropietario) {
            Button btnBorrar = new Button("🗑");
            btnBorrar.setStyle(
                    "-fx-background-color: transparent; " +
                            "-fx-text-fill: #cc0000; " +
                            "-fx-cursor: hand; " +
                            "-fx-font-size: 12px; " +
                            "-fx-padding: 0 0 0 6px;");
            btnBorrar.setTooltip(new Tooltip("Eliminar mensaje"));
            btnBorrar.setOnAction(e -> confirmarYBorrar(mensaje.getId()));
            filaInferior.getChildren().add(btnBorrar);
        }

        burbuja.getChildren().addAll(lblNombre, lblTexto, filaInferior);

        HBox alineacion = new HBox();
        if (miNombreUsuario != null &&
                mensaje.getNombreUsuario().equalsIgnoreCase(miNombreUsuario)) {
            burbuja.getStyleClass().add("burbuja-mia");
            alineacion.setAlignment(Pos.CENTER_RIGHT);
        } else {
            burbuja.getStyleClass().add("burbuja-otro");
            alineacion.setAlignment(Pos.CENTER_LEFT);
        }
        alineacion.getChildren().add(burbuja);
        return alineacion;
    }

    private void confirmarYBorrar(Long idMensaje) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar mensaje");
        confirmacion.setHeaderText("¿Eliminar este mensaje?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                new Thread(() -> {
                    boolean ok = clienteService.borrarMensaje(idMensaje, miNombreUsuario);
                    Platform.runLater(() -> {
                        if (ok) {
                            actualizarMensajes();
                        } else {
                            new Alert(Alert.AlertType.ERROR,
                                    "No se pudo eliminar el mensaje.")
                                    .showAndWait();
                        }
                    });
                }).start();
            }
        });
    }

    @FXML
    void enviarMensaje(ActionEvent event) {
        String texto = txtInputMensaje.getText();
        if (texto != null && !texto.trim().isEmpty() && miNombreUsuario != null) {
            String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            Mensaje nuevo = new Mensaje(texto, miNombreUsuario, LocalDate.now(), hora);
            clienteService.enviarNuevoMensaje(nuevo);
            txtInputMensaje.clear();
            actualizarMensajes();
        }
    }

    @FXML
    void salirAplicacion(ActionEvent event) {
        System.exit(0);
    }
}