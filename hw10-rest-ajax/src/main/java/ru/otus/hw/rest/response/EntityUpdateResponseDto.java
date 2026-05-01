package ru.otus.hw.rest.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class EntityUpdateResponseDto<T> {

    @Nullable
    private final T entity;

    @Nullable
    private final List<ErrorItem> requestValidationErrors;

    public record ErrorItem(String code, String text) { }
}
