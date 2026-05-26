package org.iesalandalus.ChatDAM.javafx;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stagePrimario) {
        LoginVentana loginVentana = new LoginVentana();
        loginVentana.mostrar(stagePrimario);
    }

    public static void main(String[] args) {
        launch(args);
    }
}