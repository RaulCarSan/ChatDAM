package org.iesalandalus.ChatDAM.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CifradoUtil {

    // Clave AES de 16 caracteres — misma en cliente y servidor
    private static final String CLAVE_AES = "ChatDAM2025Clave";

    public static String cifrar(String texto) throws Exception {
        SecretKeySpec clave = new SecretKeySpec(CLAVE_AES.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, clave);
        byte[] textoCifrado = cipher.doFinal(texto.getBytes());
        return Base64.getEncoder().encodeToString(textoCifrado);
    }

    public static String descifrar(String textoCifrado) throws Exception {
        SecretKeySpec clave = new SecretKeySpec(CLAVE_AES.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, clave);
        byte[] textoDescifrado = cipher.doFinal(Base64.getDecoder().decode(textoCifrado));
        return new String(textoDescifrado);
    }

    // Hash SHA-256 para almacenar/comparar contraseñas en base de datos
    public static String hashSHA256(String texto) throws Exception {
        java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(texto.getBytes("UTF-8"));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}