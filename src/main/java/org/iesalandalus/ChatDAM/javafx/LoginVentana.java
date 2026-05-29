package org.iesalandalus.ChatDAM.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.iesalandalus.ChatDAM.chat.cliente.AuthClienteService;
import org.iesalandalus.ChatDAM.chat.controller.ChatControlador;
import org.iesalandalus.ChatDAM.chat.dto.LoginResponse;

import java.io.IOException;

public class LoginVentana {

    public void mostrar(Stage stage) {
        stage.setTitle("ChatDAM - Iniciar sesión");

        Label lblTitulo   = new Label("ChatDAM - Login Corporativo");
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label lblUsuario   = new Label("Usuario:");
        TextField tfUsuario = new TextField();
        tfUsuario.setPromptText("Introduce tu usuario");

        Label lblPassword         = new Label("Contraseña:");
        PasswordField pfPassword  = new PasswordField();
        pfPassword.setPromptText("Introduce tu contraseña");

        Label lblError = new Label();
        lblError.setStyle("-fx-text-fill: red;");

        Button btnLogin = new Button("Entrar");
        btnLogin.setDefaultButton(true);
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnLogin.setStyle(
                "-fx-background-color: #128C7E; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-background-radius: 5px;"
        );

        Button btnRegistro = new Button("¿No tienes cuenta? Regístrate");
        btnRegistro.setMaxWidth(Double.MAX_VALUE);
        btnRegistro.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #128C7E; " +
                        "-fx-border-color: #128C7E; -fx-border-radius: 5px; -fx-background-radius: 5px;"
        );
        btnRegistro.setOnAction(e -> {
            RegistroVentana ventanaRegistro = new RegistroVentana();
            ventanaRegistro.mostrar(stage);
        });

        btnLogin.setOnAction(e -> {
            String usuario  = tfUsuario.getText().trim();
            String password = pfPassword.getText();

            if (usuario.isEmpty() || password.isEmpty()) {
                lblError.setText("Por favor, rellena todos los campos.");
                return;
            }

            btnLogin.setDisable(true);
            btnRegistro.setDisable(true);
            lblError.setText("Validando...");
            lblError.setStyle("-fx-text-fill: #128C7E;");

            new Thread(() -> {
                AuthClienteService authService = new AuthClienteService();
                LoginResponse respuesta = authService.login(usuario, password);

                javafx.application.Platform.runLater(() -> {
                    btnLogin.setDisable(false);
                    btnRegistro.setDisable(false);
                    lblError.setStyle("-fx-text-fill: red;");

                    if (respuesta != null && respuesta.isExito()) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista_chat.fxml"));
                            Parent root = loader.load();

                            ChatControlador controlador = loader.getController();
                            controlador.setNombreUsuario(respuesta.getNombreUsuario(), respuesta.getRol());
                            stage.setTitle("ChatDAM - " + respuesta.getNombreUsuario());
                            stage.setScene(new Scene(root));
                            stage.show();

                        } catch (IOException ex) {
                            lblError.setText("Error al cargar la vista del chat.");
                            ex.printStackTrace();
                        }

                    } else {
                        String motivo = (respuesta != null)
                                ? respuesta.getMensaje()
                                : "Sin conexión con el servidor";
                        ErrorVentana errorVentana = new ErrorVentana(motivo);
                        errorVentana.mostrar();
                    }
                });
            }).start();
        });

        Separator separador = new Separator();
        separador.setStyle("-fx-padding: 4 0 0 0;");

        VBox layout = new VBox(10,
                lblTitulo,
                lblUsuario, tfUsuario,
                lblPassword, pfPassword,
                lblError,
                btnLogin,
                separador,
                btnRegistro
        );
        layout.setPadding(new Insets(24));
        layout.setAlignment(Pos.CENTER);
        layout.setMinWidth(300);

        stage.setScene(new Scene(layout, 360, 340));
        stage.setResizable(false);
        stage.show();
    }
}