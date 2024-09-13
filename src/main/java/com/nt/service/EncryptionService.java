package com.nt.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
public class EncryptionService {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int KEY_LENGTH = 256;
    private static final int ITERATION_COUNT = 65536;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int IV_LENGTH = 16;

    // Generate a random salt
    public String generateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // Derive secret key using PBKDF2 with salt
    private SecretKey getSecretKey(String password, String salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), Base64.getDecoder().decode(salt), ITERATION_COUNT, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
    }

    // Encrypt the data
    public String encrypt(String data, String password, String salt) throws Exception {
        SecretKeySpec keySpec = (SecretKeySpec) getSecretKey(password, salt);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        // Generate IV
        byte[] iv = new byte[IV_LENGTH];
        RANDOM.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());

        // Return IV + encrypted text encoded in Base64
        byte[] combined = new byte[IV_LENGTH + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, IV_LENGTH);
        System.arraycopy(encryptedBytes, 0, combined, IV_LENGTH, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    // Decrypt the data
    public String decrypt(String encryptedData, String password, String salt) throws Exception {
        if (encryptedData == null || encryptedData.isEmpty()) {
            throw new IllegalArgumentException("Encrypted data cannot be null or empty");
        }

        byte[] combined;
        try {
            combined = Base64.getDecoder().decode(encryptedData);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64 encoded data: " + encryptedData, e);
        }

        if (combined.length < IV_LENGTH) {
            throw new IllegalArgumentException("Invalid encrypted data length");
        }

        // Extract the IV from the beginning of the combined byte array
        byte[] iv = new byte[IV_LENGTH];
        System.arraycopy(combined, 0, iv, 0, IV_LENGTH);

        // Extract the encrypted bytes
        byte[] encryptedBytes = new byte[combined.length - IV_LENGTH];
        System.arraycopy(combined, IV_LENGTH, encryptedBytes, 0, encryptedBytes.length);

        SecretKeySpec keySpec = (SecretKeySpec) getSecretKey(password, salt);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        // Use the IV with the decryption cipher
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] decryptedBytes;
        try {
            decryptedBytes = cipher.doFinal(encryptedBytes);
        } catch (Exception e) {
            // Log or handle the exception properly
            throw new RuntimeException("Decryption failed", e);
        }

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

}
