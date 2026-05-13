package ru.otus.hw.sequrity.crypto;

import jakarta.validation.constraints.NotBlank;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "app.security")
@Validated
public class AppCryptoConfiguration {

    @Setter
    @NotBlank(message = "{auth_key_password_not_provided}")
    private String authKeyPassword;

    @Setter
    @NotBlank(message = "{auth_key_salt_not_provided}")
    private String authKeySalt;

    @Bean
    public PasswordEncoder appPasswordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>(2);

        encoders.put(
                StribogPasswordEncoder.ALGO_NAME,
                StribogPasswordEncoder.getInstance(authKeyPassword, authKeySalt)
        );
        encoders.put(
                KuznezikPasswordEncoder.ALGO_NAME,
                KuznezikPasswordEncoder.getInstance(authKeyPassword, authKeySalt)
        );

        return new DelegatingPasswordEncoder(StribogPasswordEncoder.ALGO_NAME, encoders);
    }
}
