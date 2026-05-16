package ru.otus.hw.sequrity.crypto;

import org.rssys.gost.api.Digest;
import org.rssys.gost.api.KeyGenerator;
import org.rssys.gost.cipher.SymmetricKey;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Base64;

public class StribogPasswordEncoder implements PasswordEncoder {

    public static final String ALGO_NAME = "stribog";

    private byte[] password;

    private byte[] salt;

    private StribogPasswordEncoder() {

    }

    public static StribogPasswordEncoder getInstance(String password, String salt) {
        var instance = new StribogPasswordEncoder();
        instance.password = password.getBytes(StandardCharsets.UTF_8);
        instance.salt = salt.getBytes(StandardCharsets.UTF_8);
        return instance;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        byte[] plainText = rawPassword.toString().getBytes(StandardCharsets.UTF_8);
        SymmetricKey key = null;
        try {
            key = KeyGenerator.deriveKey(password, salt);
            byte[] hash = Digest.hmac512(plainText, key);
            return Base64.getEncoder().encodeToString(hash);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("StribogPasswordEncoder->encode() error get Key", e);
        } finally {
            if (key != null) {
                key.destroy();
            }
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        byte[] rawBytes = rawPassword.toString().getBytes(StandardCharsets.UTF_8);
        SymmetricKey key = null;
        try {
            key = KeyGenerator.deriveKey(password, salt);
            byte[] hash = Digest.hmac512(rawBytes, key);
            byte[] originalHash = Base64.getDecoder().decode(encodedPassword);
            return MessageDigest.isEqual(originalHash, hash);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("StribogPasswordEncoder->matches() error get Key", e);
        } finally {
            if (key != null) {
                key.destroy();
            }
        }
    }
}
