package ru.otus.hw.sequrity.userdetails;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.otus.hw.repositories.ReaderRepository;

@Configuration
public class AppUserDetailsConfiguration {

    @Bean
    public UserDetailsService userDetailsService(ReaderRepository readerRepository) {
        return new UserDetailsServiceImpl(readerRepository);
    }
}
