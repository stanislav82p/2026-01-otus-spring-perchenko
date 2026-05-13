package ru.otus.hw.sequrity.crypto;

import org.rssys.gost.api.AuthenticatedCipher;
import org.rssys.gost.api.KeyGenerator;
import org.rssys.gost.cipher.SymmetricKey;
import org.rssys.gost.util.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Base64;

public class KuznezikPasswordEncoder implements PasswordEncoder {

    public static final String ALGO_NAME = "kuzya";

    private byte[] password;

    private byte[] salt;

    private KuznezikPasswordEncoder() {
    }

    public static KuznezikPasswordEncoder getInstance(String password, String salt) {
        var instance = new KuznezikPasswordEncoder();
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
            byte[] encrypted = AuthenticatedCipher.seal(plainText, key);
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("KuznezikPasswordEncoder->encode() error get Key", e);
        } finally {
            if (key != null) {
                key.destroy();
            }
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        byte[] rawBytes = rawPassword.toString().getBytes(StandardCharsets.UTF_8);
        byte[] closedPwdBytes = Base64.getDecoder().decode(encodedPassword);
        SymmetricKey key = null;
        try {
            key = KeyGenerator.deriveKey(password, salt);
            byte[] openPwdBytes = AuthenticatedCipher.open(closedPwdBytes, key);
            return MessageDigest.isEqual(openPwdBytes, rawBytes);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("KuznezikPasswordEncoder->matches() error get Key", e);
        } catch (AuthenticationException e) {
            throw new RuntimeException("KuznezikPasswordEncoder->matches() user password in DB is corrupted", e);
        } finally {
            if (key != null) {
                key.destroy();
            }
        }
    }
}
