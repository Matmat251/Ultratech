package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * Utilidad para cifrado de contraseñas usando SHA-256
 */
public class PasswordUtils {

    /**
     * Genera un hash SHA-256 de la contraseña
     * 
     * @param password la contraseña en texto plano
     * @return el hash en formato hexadecimal
     */
    public static String hash(String password) {
        if (password == null || password.isEmpty()) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error: SHA-256 no disponible - " + e.getMessage());
            return password; // Fallback: devuelve la contraseña sin cifrar
        }
    }

    /**
     * Verifica si una contraseña coincide con su hash
     * 
     * @param password       la contraseña en texto plano
     * @param hashedPassword el hash almacenado
     * @return true si coinciden
     */
    public static boolean verify(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) {
            return false;
        }
        // Si el hash almacenado tiene longitud de 64 (SHA-256 hex), verificar con hash
        if (hashedPassword.length() == 64) {
            String inputHash = hash(password);
            return inputHash.equals(hashedPassword);
        }
        // Compatibilidad con contraseñas antiguas no cifradas
        return password.equals(hashedPassword);
    }

    /**
     * Convierte bytes a string hexadecimal
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Verifica si una contraseña ya está cifrada (tiene formato de hash SHA-256)
     */
    public static boolean isHashed(String password) {
        if (password == null)
            return false;
        // SHA-256 produce un hash de 64 caracteres hexadecimales
        return password.length() == 64 && password.matches("[a-f0-9]+");
    }
}
