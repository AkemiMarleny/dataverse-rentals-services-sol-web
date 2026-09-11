package com.dataverse.dataverserentals.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

@Component
public class MfaSecretEncryptor {

    private final TextEncryptor textEncryptor;

    public MfaSecretEncryptor(
            @Value("${dataverse.mfa.encryptor.password}") String password,
            @Value("${dataverse.mfa.encryptor.salt}") String salt) {
        this.textEncryptor = Encryptors.delux(password, salt);
    }

    public String encrypt(String plainText) {
        if (plainText == null || plainText.isBlank()) {
            return plainText;
        }
        return textEncryptor.encrypt(plainText);
    }

    public String decrypt(String encryptedText) {
        if (encryptedText == null || encryptedText.isBlank()) {
            return encryptedText;
        }
        return textEncryptor.decrypt(encryptedText);
    }

    public String reencryptAndConsumeBackupCodes(String encryptedCodes, String usedCode) {
        String decryptedCodes = decrypt(encryptedCodes);
        String normalizedUsed = usedCode.trim().toUpperCase();
        String[] codes = decryptedCodes.split(",");
        StringBuilder remaining = new StringBuilder();
        for (String code : codes) {
            if (!code.trim().equals(normalizedUsed)) {
                if (remaining.length() > 0) {
                    remaining.append(",");
                }
                remaining.append(code.trim());
            }
        }
        if (remaining.length() == 0) {
            return null;
        }
        return encrypt(remaining.toString());
    }
}
