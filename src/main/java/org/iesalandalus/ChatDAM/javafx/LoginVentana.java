package org.iesalandalus.ChatDAM.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.iesalandalus.ChatDAM.chat.cliente.AuthClienteService;
import org.iesalandalus.ChatDAM.chat.controller.ChatControlador;
import org.iesalandalus.ChatDAM.chat.dto.LoginResponse;

import java.io.IOException;

public class LoginVentana {

    public void mostrar(Stage stage) {
        stage.setTitle("ChatDAM - Iniciar sesión");

        Label lblTitulo    = new Label("ChatDAM - Login Corporativo");
        Label lblUsuario   = new Label("Usuario:");
        TextField tfUsuario = new TextField();
        tfUsuario.setPromptText("Introduce tu usuario");

        Label lblPassword         = new Label("Contraseña:");
        PasswordField pfPassword  = new PasswordField();
        pfPassword.setPromptText("Introduce tu contraseña");

        Label lblError = new Label();
        lblError.setStyle("-fx-text-fill: red;");

        Button btnLogin = new Button("Entrar");
        btnLogin.setDefaultButton(true); // responde a Enter

        btnLogin.setOnAction(e -> {
            String usuario  = tfUsuario.getText().trim();
            String password = pfPassword.getText();

            if (usuario.isEmpty() || password.isEmpty()) {
                lblError.setText("Por favor, rellena todos los campos.");
                return;
            }

            btnLogin.setDisable(true);
            lblError.setText("Validando...");

            // Llamada en hilo separado para no bloquear la UI de JavaFX
            new Thread(() -> {
                AuthClienteService authService = new AuthClienteService();
                LoginResponse respuesta = authService.login(usuario, password);

                javafx.application.Platform.runLater(() -> {
                    btnLogin.setDisable(false);
                    if (respuesta != null && respuesta.isExito()) {

                        // Login correcto → cargamos la vista FXML directamente
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista_chat.fxml"));
                            Parent root = loader.load();

                            // Pasamos el nombre del login al controlador directamente
                            ChatControlador controlador = loader.getController();
                            controlador.setNombreUsuario(respuesta.getNombreUsuario());

                            stage.setTitle("ChatDAM - " + respuesta.getNombreUsuario());
                            stage.setScene(new Scene(root));
                            stage.show();

                        } catch (IOException ex) {
                            lblError.setText("Error al cargar la vista del chat.");
                            ex.printStackTrace();
                        }

                    } else {
                        // Login incorrecto → ventana de error
                        String motivo = (respuesta != null) ? respuesta.getMensaje() : "Sin conexión con el servidor";
                        ErrorVentana errorVentana = new ErrorVentana(motivo);
                        errorVentana.mostrar();
                    }
                });
            }).start();
        });

        VBox layout = new VBox(10, lblTitulo, lblUsuario, tfUsuario,
                lblPassword, pfPassword, lblError, btnLogin);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setMinWidth(300);

        stage.setScene(new Scene(layout, 350, 280));
        stage.setResizable(false);
        stage.show();
    }
}