package ru.otus.hw.rest.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import ru.otus.hw.models.dto.BookDto;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class BookUpdateResponseDto {

    @Nullable
    private final BookDto book;

    @Nullable
    private final List<ErrorItem> requestValidationErrors;

    public record ErrorItem(String code, String text) { }
}
