package org.iesalandalus.ChatDAM.chat.cliente;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class ChatApp extends Application {

    @Override
    public void start(Stage escenarioPrincipal) {
        try {
            URL fxmlUrl = getClass().getResource("/vista_chat.fxml");

            if (fxmlUrl == null) {
                throw new RuntimeException("No se ha encontrado el archivo vista_chat.fxml en resources");
            }

            FXMLLoader cargador = new FXMLLoader(fxmlUrl);
            Parent raiz = cargador.load();

            Scene escena = new Scene(raiz);
            URL cssUrl = getClass().getResource("/estilos.css");

            if (cssUrl != null) {
                escena.getStylesheets().add(cssUrl.toExternalForm());
            }

            escenarioPrincipal.setTitle("Chat Corporativo DAM - Conectado a AWS");


            escenarioPrincipal.setScene(escena);

            escenarioPrincipal.setResizable(false);

            escenarioPrincipal.show();

        } catch (Exception e) {
            System.err.println("Error fatal al arrancar la aplicación de escritorio:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
