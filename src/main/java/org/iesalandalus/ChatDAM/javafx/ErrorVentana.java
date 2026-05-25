package org.iesalandalus.ChatDAM.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ErrorVentana {

    private final String motivo;

    public ErrorVentana(String motivo) {
        this.motivo = motivo;
    }

    public void mostrar() {
        Stage stage = new Stage();
        stage.setTitle("Error de autenticación");
        stage.initModality(Modality.APPLICATION_MODAL);

        Label lblIcono  = new Label("✗");
        lblIcono.setStyle("-fx-font-size: 40px; -fx-text-fill: red;");

        Label lblTitulo = new Label("Acceso denegado");
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label lblMotivo = new Label(motivo);
        lblMotivo.setStyle("-fx-text-fill: gray;");

        Button btnSalir = new Button("Salir");
        btnSalir.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnSalir.setOnAction(e -> {
            // Cierra toda la aplicación JavaFX
            javafx.application.Platform.exit();
            System.exit(0);
        });

        VBox layout = new VBox(15, lblIcono, lblTitulo, lblMotivo, btnSalir);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(layout, 320, 220));
        stage.setResizable(false);
        stage.show();
    }
}