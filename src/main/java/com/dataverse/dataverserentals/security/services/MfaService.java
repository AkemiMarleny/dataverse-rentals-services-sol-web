package com.dataverse.dataverserentals.security.services;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Service;

@Service
public class MfaService {

    private static final int TIME_STEP = 30;
    private static final int CODE_DIGITS = 6;
    private static final String HMAC_ALGORITHM = "HmacSHA1";

    public String generateMfaSecret() {
        byte[] buffer = new byte[20];
        new SecureRandom().nextBytes(buffer);
        Base32 base32 = new Base32();
        return base32.encodeToString(buffer);
    }

    public String generateQrCodeUrl(String secret, String username, String issuer) {
        return String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s&digits=%d&period=%d",
                issuer, username, secret, issuer, CODE_DIGITS, TIME_STEP);
    }

    public boolean validateTotpCode(String secret, int code) {
        byte[] secretBytes = new Base32().decode(secret);
        long timeWindow = System.currentTimeMillis() / 1000 / TIME_STEP;

        for (int i = -1; i <= 1; i++) {
            int generatedCode = generateCode(secretBytes, timeWindow + i);
            if (generatedCode == code) {
                return true;
            }
        }
        return false;
    }

    private int generateCode(byte[] secret, long timeWindow) {
        try {
            byte[] timeBytes = ByteBuffer.allocate(8).putLong(timeWindow).array();
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec signKey = new SecretKeySpec(secret, HMAC_ALGORITHM);
            mac.init(signKey);
            byte[] hash = mac.doFinal(timeBytes);

            int offset = hash[hash.length - 1] & 0xF;
            int binary = ((hash[offset] & 0x7F) << 24) |
                    ((hash[offset + 1] & 0xFF) << 16) |
                    ((hash[offset + 2] & 0xFF) << 8) |
                    (hash[offset + 3] & 0xFF);

            return binary % (int) Math.pow(10, CODE_DIGITS);
        } catch (Exception e) {
            throw new RuntimeException("Error generating TOTP code", e);
        }
    }

    public List<String> generateBackupCodes() {
        List<String> backupCodes = new ArrayList<>();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 10; i++) {
            byte[] bytes = new byte[4];
            random.nextBytes(bytes);
            String code = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            backupCodes.add(code.toUpperCase());
        }
        return backupCodes;
    }

    public boolean verifyBackupCode(String storedBackupCodes, String providedCode) {
        if (storedBackupCodes == null || storedBackupCodes.isEmpty()) {
            return false;
        }
        String normalizedProvided = providedCode.trim().toUpperCase();
        String[] codes = storedBackupCodes.split(",");
        for (String code : codes) {
            if (code.trim().equals(normalizedProvided)) {
                return true;
            }
        }
        return false;
    }

    public String encodeBackupCodes(List<String> codes) {
        return String.join(",", codes);
    }

    public List<String> decodeBackupCodes(String codes) {
        return Arrays.asList(codes.split(","));
    }

    public boolean isPlaintextBase32(String secret) {
        if (secret == null || secret.isBlank()) {
            return false;
        }
        try {
            new Base32().decode(secret);
            return secret.matches("^[A-Z2-7]+=*$");
        } catch (Exception e) {
            return false;
        }
    }
}
