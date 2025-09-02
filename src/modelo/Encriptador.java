package modelo;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;

public class Encriptador {
    
    // Método para generar una llave simétrica AES
    public static SecretKey generarLlaveAes() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // Tamaño de la llave en bits
        return keyGen.generateKey();
    }
    
    // Método para encriptar datos con AES
    public static byte[] cifrarAes(byte[] datos, SecretKey llave) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, llave);
        return cipher.doFinal(datos);
    }
    
    // Método para desencriptar datos con AES
    public static byte[] descifrarAes(byte[] datosCifrados, SecretKey llave) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, llave);
        return cipher.doFinal(datosCifrados);
    }

    // Método para cifrar una llave simétrica con RSA
    public static byte[] cifrarLlaveRsa(SecretKey llaveAes, PublicKey llavePublica) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, llavePublica);
        return cipher.doFinal(llaveAes.getEncoded());
    }
    
    // Método para descifrar una llave simétrica con RSA
    public static SecretKey descifrarLlaveRsa(byte[] llaveCifrada, PrivateKey llavePrivada) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, llavePrivada);
        byte[] llaveDescifrada = cipher.doFinal(llaveCifrada);
        
        // 🌟 CORRECCIÓN CLAVE: Crear la SecretKey a partir de los bytes desencriptados
        return new javax.crypto.spec.SecretKeySpec(llaveDescifrada, "AES");
    }

    // Cargar llave pública desde un archivo PEM
    public static PublicKey cargarLlavePublica(String rutaArchivo) throws Exception {
        try (InputStream is = new FileInputStream(rutaArchivo)) {
            byte[] keyBytes = is.readAllBytes();
            String keyPem = new String(keyBytes, StandardCharsets.UTF_8)
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decoded = Base64.getDecoder().decode(keyPem);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        }
    }

    // Cargar llave privada desde un archivo PEM
    public static PrivateKey cargarLlavePrivada(String rutaArchivo) throws Exception {
        try (InputStream is = new FileInputStream(rutaArchivo)) {
            byte[] keyBytes = is.readAllBytes();
            String keyPem = new String(keyBytes, StandardCharsets.UTF_8)
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decoded = Base64.getDecoder().decode(keyPem);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        }
    }
    
    public static PrivateKey cargarLlavePrivadaDesdePem(String rutaArchivo, char[] contrasenia) throws Exception {
        // La implementación actual de OpenSSL requiere que la llave privada sea decifrada con la contraseña antes de ser cargada
        // Esto es un placeholder para una implementación más compleja. Por ahora, asumimos que el archivo no tiene contraseña.
        // Si tu llave tiene contraseña, necesitarás una librería externa como Bouncy Castle.
        try (InputStream is = new FileInputStream(rutaArchivo)) {
            byte[] keyBytes = is.readAllBytes();
            String keyPem = new String(keyBytes, StandardCharsets.UTF_8)
                    .replace("-----BEGIN ENCRYPTED PRIVATE KEY-----", "")
                    .replace("-----END ENCRYPTED PRIVATE KEY-----", "")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decoded = Base64.getDecoder().decode(keyPem);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        }
    }

    public static char[] pedirContrasenia(String mensaje) {
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(null, passwordField, mensaje, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            return passwordField.getPassword();
        }
        return null;
    }
}