package org.iesalandalus.ChatDAM.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.iesalandalus.ChatDAM.chat.cliente.RegistroClienteService;
import org.iesalandalus.ChatDAM.chat.dto.RegistroResponse;

public class RegistroVentana {

    public void mostrar(Stage owner) {
        Stage stage = new Stage();
        stage.setTitle("ChatDAM - Crear cuenta");
        // Modal: bloquea la ventana de login mientras está abierta
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(owner);

        // --- Título ---
        Label lblTitulo = new Label("Crear nueva cuenta");
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // --- Usuario ---
        Label lblUsuario = new Label("Nombre de usuario:");
        TextField tfUsuario = new TextField();
        tfUsuario.setPromptText("Solo letras, sin números ni símbolos");

        // --- Contraseña ---
        Label lblPassword = new Label("Contraseña (mín. 6 caracteres):");
        PasswordField pfPassword = new PasswordField();
        pfPassword.setPromptText("Introduce una contraseña");

        // --- Confirmar contraseña ---
        Label lblConfirm = new Label("Confirmar contraseña:");
        PasswordField pfConfirm = new PasswordField();
        pfConfirm.setPromptText("Repite la contraseña");

        // --- Mensaje de error/éxito ---
        Label lblMensaje = new Label();

        // --- Botones ---
        Button btnRegistrar = new Button("Crear cuenta");
        btnRegistrar.setDefaultButton(true);
        btnRegistrar.setMaxWidth(Double.MAX_VALUE);
        btnRegistrar.setStyle("-fx-background-color: #128C7E; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;");

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setMaxWidth(Double.MAX_VALUE);
        btnCancelar.setOnAction(e -> stage.close());

        btnRegistrar.setOnAction(e -> {
            String usuario  = tfUsuario.getText().trim();
            String password = pfPassword.getText();
            String confirm  = pfConfirm.getText();

            // --- Validaciones en cliente (antes de llamar al servidor) ---
            if (usuario.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                mostrarMensaje(lblMensaje, "Rellena todos los campos.", false);
                return;
            }

            if (!usuario.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
                mostrarMensaje(lblMensaje, "El usuario solo puede contener letras.", false);
                return;
            }

            if (password.length() < 6) {
                mostrarMensaje(lblMensaje, "La contraseña debe tener al menos 6 caracteres.", false);
                return;
            }

            if (!password.equals(confirm)) {
                mostrarMensaje(lblMensaje, "Las contraseñas no coinciden.", false);
                pfConfirm.clear();
                return;
            }

            btnRegistrar.setDisable(true);
            mostrarMensaje(lblMensaje, "Procesando...", true);

            // Llamada en hilo separado para no bloquear la UI
            final String usuarioFinal  = usuario;
            final String passwordFinal = password;
            new Thread(() -> {
                RegistroClienteService service = new RegistroClienteService();
                RegistroResponse respuesta = service.registrar(usuarioFinal, passwordFinal);

                javafx.application.Platform.runLater(() -> {
                    btnRegistrar.setDisable(false);

                    if (respuesta != null && respuesta.isExito()) {
                        // Registro correcto: mostrar mensaje de éxito y cerrar tras un momento
                        mostrarMensaje(lblMensaje, "✓ " + respuesta.getMensaje(), true);
                        btnRegistrar.setDisable(true);
                        btnCancelar.setText("Cerrar");
                        // Cerrar automáticamente a los 2 segundos
                        javafx.animation.PauseTransition pausa =
                                new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));
                        pausa.setOnFinished(ev -> stage.close());
                        pausa.play();
                    } else {
                        String motivo = (respuesta != null)
                                ? respuesta.getMensaje()
                                : "Sin conexión con el servidor.";
                        mostrarMensaje(lblMensaje, motivo, false);
                    }
                });
            }).start();
        });

        // --- Layout ---
        VBox layout = new VBox(10,
                lblTitulo,
                new Separator(),
                lblUsuario, tfUsuario,
                lblPassword, pfPassword,
                lblConfirm, pfConfirm,
                lblMensaje,
                btnRegistrar,
                btnCancelar
        );
        layout.setPadding(new Insets(24));
        layout.setAlignment(Pos.CENTER_LEFT);
        layout.setMinWidth(320);

        stage.setScene(new Scene(layout, 360, 420));
        stage.setResizable(false);
        stage.show();
    }

    /** Muestra el mensaje con color verde (éxito) o rojo (error). */
    private void mostrarMensaje(Label lbl, String texto, boolean exito) {
        lbl.setText(texto);
        lbl.setStyle(exito
                ? "-fx-text-fill: #128C7E; -fx-font-weight: bold;"
                : "-fx-text-fill: #e74c3c;");
    }
}