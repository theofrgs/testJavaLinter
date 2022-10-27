package com.outside;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.security.MessageDigest;

@org.springframework.stereotype.Service
public class EncryptService extends Service {
    private static SecretKeySpec secretKey;

    public EncryptService() {
        // init SecretKeySpec
        this.initLogger(EncryptService.class);
        try {
            // transform key in bytes
            if (properties.getProperty("password.secret") == null) {
                throw new RuntimeException("Cannot load the password.secret from properties. Please ensure you provide it in services.properties.");
            }
            byte[] key = properties.getProperty("password.secret").getBytes(StandardCharsets.UTF_8);
            // get instance to hash password
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            // hash passowrd
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            this.LOGGER.error(ExceptionUtils.getMessage(e));
        }
    }

    private Cipher getCipherInstance(int cipherMode) throws Exception {
        Cipher cipherInstance = Cipher.getInstance("AES/ECB/PKCS5Padding");

        cipherInstance.init(cipherMode, secretKey);
        return cipherInstance;
    }

    public String encryptStr(String str) {
        try {
            Cipher encrypt = getCipherInstance(Cipher.ENCRYPT_MODE);
            return Base64.getEncoder()
                    .encodeToString(encrypt.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            this.LOGGER.error(ExceptionUtils.getMessage(e));
        }
        return null;
    }

    public String decryptStr(String encryptedStr) {
        try {
            Cipher decrypt = getCipherInstance(Cipher.DECRYPT_MODE);
            return new String(decrypt.doFinal(Base64.getDecoder()
                    .decode(encryptedStr)));
        } catch (Exception e) {
            this.LOGGER.error(ExceptionUtils.getMessage(e));
        }
        return null;
    }
}