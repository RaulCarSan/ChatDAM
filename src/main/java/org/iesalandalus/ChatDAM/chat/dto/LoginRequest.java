package org.iesalandalus.ChatDAM.chat.dto;

public class LoginRequest {
    private String nombreUsuarioCifrado;
    private String passwordCifrado;

    public LoginRequest() {}

    public String getNombreUsuarioCifrado() { return nombreUsuarioCifrado; }
    public void setNombreUsuarioCifrado(String v) { this.nombreUsuarioCifrado = v; }
    public String getPasswordCifrado() { return passwordCifrado; }
    public void setPasswordCifrado(String v) { this.passwordCifrado = v; }
}