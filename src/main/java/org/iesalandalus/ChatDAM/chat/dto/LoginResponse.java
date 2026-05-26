package org.iesalandalus.ChatDAM.chat.dto;

public class LoginResponse {
    private boolean exito;
    private String mensaje;
    private String nombreUsuario; // Solo se devuelve si el login fue exitoso

    public LoginResponse() {}

    public LoginResponse(boolean exito, String mensaje, String nombreUsuario) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.nombreUsuario = nombreUsuario;
    }

    public boolean isExito() { return exito; }
    public String getMensaje() { return mensaje; }
    public String getNombreUsuario() { return nombreUsuario; }
}