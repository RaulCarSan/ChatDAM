package org.iesalandalus.ChatDAM.chat.dto;

public class RegistroRequest {
    // Campos cifrados con AES desde JavaFX antes de enviar
    private String nombreUsuarioCifrado;
    private String passwordCifrado;

    public RegistroRequest() {}

    public String getNombreUsuarioCifrado() { return nombreUsuarioCifrado; }
    public void setNombreUsuarioCifrado(String v) { this.nombreUsuarioCifrado = v; }
    public String getPasswordCifrado() { return passwordCifrado; }
    public void setPasswordCifrado(String v) { this.passwordCifrado = v; }
}