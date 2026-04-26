package ru.otus.hw.rest.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.models.dto.BookDto;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class GetBooksResponseDto {
    private final List<BookDto> books;

    private final String summary;

    private final String actionDetails;

    private final String actionDelete;
}
