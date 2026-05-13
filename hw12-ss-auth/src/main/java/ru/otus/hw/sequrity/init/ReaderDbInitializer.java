package ru.otus.hw.sequrity.init;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.entity.ReaderEntity;
import ru.otus.hw.repositories.ReaderRepository;
import ru.otus.hw.sequrity.UserRole;
import ru.otus.hw.sequrity.crypto.KuznezikPasswordEncoder;
import ru.otus.hw.sequrity.crypto.StribogPasswordEncoder;

import java.util.List;

/**
 * Это тестовый класс, который забивает тестовых пользаков в БД.
 * Делаем это в рантайме, чтобы посчитать хэши паролей
 */
@Component
@ConfigurationProperties(prefix = "app.security")
public class ReaderDbInitializer {

    private final ReaderRepository readerRepository;

    @Setter
    private String authKeyPassword;

    @Setter
    private String authKeySalt;

    @Autowired
    public ReaderDbInitializer(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initializeReaders() {
        var pwdEncoder1 = StribogPasswordEncoder.getInstance(authKeyPassword, authKeySalt);
        var pwdEncoder2 = KuznezikPasswordEncoder.getInstance(authKeyPassword, authKeySalt);
        var reader1 = new ReaderEntity("usr1", "firstName_1", "lastName_1",
                encodePasswordStribog(pwdEncoder1, "password-1"), UserRole.PUBLIC);
        System.out.printf("Created reader (password = %s): %s\n", "password-1", reader1);
        var reader2 = new ReaderEntity("usr2", "firstName_2", "lastName_2",
                encodePasswordStribog(pwdEncoder1, "password-2"), UserRole.READER);
        System.out.printf("Created reader (password = %s): %s\n", "password-2", reader2);
        var reader3 = new ReaderEntity("usr3", "firstName_3", "lastName_3",
                encodePasswordKuznezik(pwdEncoder2, "password-3"), UserRole.COMMENTER);
        System.out.printf("Created reader (password = %s): %s\n", "password-3", reader3);
        var reader4 = new ReaderEntity("usr4", "firstName_4", "lastName_4",
                encodePasswordKuznezik(pwdEncoder2, "password-4"), UserRole.EDITOR);
        System.out.printf("Created reader (password = %s): %s\n", "password-4", reader4);
        var reader5 = new ReaderEntity("usr5", "firstName_5", "lastName_5",
                encodePasswordStribog(pwdEncoder1, "password-5"), UserRole.MODERATOR);
        System.out.printf("Created reader (password = %s): %s\n", "password-5", reader5);

        readerRepository.saveAll(List.of(reader1, reader2, reader3, reader4, reader5));
    }

    private String encodePasswordStribog(StribogPasswordEncoder encoder, String openPassword) {
        String hash = encoder.encode(openPassword);
        return "{%s}%s".formatted(StribogPasswordEncoder.ALGO_NAME, hash);
    }

    private String encodePasswordKuznezik(KuznezikPasswordEncoder encoder, String openPassword) {
        String hash = encoder.encode(openPassword);
        return "{%s}%s".formatted(KuznezikPasswordEncoder.ALGO_NAME, hash);
    }
}
