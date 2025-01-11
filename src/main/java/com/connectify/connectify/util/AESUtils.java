package com.connectify.connectify.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

import static com.connectify.connectify.enums.Static.FIXED_IV;

@Service
public class AESUtils {
    @Value("${AES.KEY}")
    private String base64Key;

    private static final String AES = "AES";
    private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12; // Kích thước IV (12 bytes)
    private static final int GCM_TAG_LENGTH = 16; // Kích thước Tag Authentication (16 bytes)
    private static final int AES_KEY_SIZE = 256; // Kích thước khóa AES (256 bits)

    private final SecretKey secretKey;

    public AESUtils() {
        System.out.println(base64Key);
        this.secretKey = decodeKeyFromBase64("q2ert0yu3iopasdf5gvzjl");
    }

    // Sinh SecretKey ngẫu nhiên
    public static SecretKey generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
            keyGenerator.init(AES_KEY_SIZE,SecureRandom.getInstanceStrong());
            return keyGenerator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException("Error generating AES key", e);
        }
    }

    // Mã hóa dữ liệu
    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            byte[] iv = generateIV(); // Initialization Vector
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            byte[] cipherText = cipher.doFinal(plainText.getBytes());
            byte[] encrypted = combine(iv, cipherText);

            return Base64.getEncoder().encodeToString(encrypted); // Trả về chuỗi mã hóa dạng Base64
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    // Giải mã dữ liệu
    public String decrypt(String encryptedText) {
        try {
            byte[] decoded = Base64.getDecoder().decode(encryptedText);
            byte[] iv = extractIV(decoded);
            byte[] cipherText = extractCipherText(decoded);

            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            // GCM parameter spec:
            //      - Xác định độ dài của Tag Authentication
            //      - Cung cấp giá trị IV
            //      - Đảm bảo an toàn trong mã hóa và giải mã
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            byte[] plainText = cipher.doFinal(cipherText);
            return new String(plainText);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }

    // Sinh IV
    private byte[] generateIV() {
        return FIXED_IV;
    }

    // Kết hợp IV và dữ liệu mã hóa
    private byte[] combine(byte[] iv, byte[] cipherText) {
        byte[] combined = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);
        return combined;
    }

    // Tách IV từ dữ liệu kết hợp
    private byte[] extractIV(byte[] encrypted) {
        byte[] iv = new byte[GCM_IV_LENGTH];
        System.arraycopy(encrypted, 0, iv, 0, iv.length);
        return iv;
    }

    // Tách dữ liệu mã hóa từ dữ liệu kết hợp
    private byte[] extractCipherText(byte[] encrypted) {
        byte[] cipherText = new byte[encrypted.length - GCM_IV_LENGTH];
        System.arraycopy(encrypted, GCM_IV_LENGTH, cipherText, 0, cipherText.length);
        return cipherText;
    }


    // Tạo SecretKey từ Base64
    private SecretKey decodeKeyFromBase64(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, AES);
    }
}
