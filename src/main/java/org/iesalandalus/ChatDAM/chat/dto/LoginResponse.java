package org.iesalandalus.ChatDAM.chat.dto;

public class LoginResponse {
    private boolean exito;
    private String mensaje;
    private String nombreUsuario;
    private String rol; // NUEVO

    public LoginResponse() {}

    public LoginResponse(boolean exito, String mensaje, String nombreUsuario, String rol) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.nombreUsuario = nombreUsuario;
        this.rol = rol;
    }

    public boolean isExito() { return exito; }
    public String getMensaje() { return mensaje; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getRol() { return rol; }
}