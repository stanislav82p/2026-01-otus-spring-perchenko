package ru.otus.hw.converters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Configuration
public class ConvConfiguration {

    @Bean
    public DateFormat provideConverterDateFormatter() {
        return new SimpleDateFormat("dd.MM.yyyy");
    }
}
