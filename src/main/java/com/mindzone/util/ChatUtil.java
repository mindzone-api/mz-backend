package com.mindzone.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.security.MessageDigest;

public class ChatUtil {

    private static SecretKeySpec getKeyFromUUID(String uuid) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(uuid.getBytes(StandardCharsets.UTF_8));
        byte[] keyBytes = new byte[16];
        System.arraycopy(key, 0, keyBytes, 0, 16);
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static String encrypt(String plainText) throws Exception {
        String key = System.getenv("CHAT_MESSAGE_ENCRYPTION_KEY");
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKey = getKeyFromUUID(key);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText) throws Exception {
        String key = System.getenv("CHAT_MESSAGE_ENCRYPTION_KEY");
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKey = getKeyFromUUID(key);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
