package ru.otus.hw.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@AllArgsConstructor
@Data
public class BookCreationDto {

    @NotBlank(message = "{book-title-not-blank}")
    @Size(min = 5, max = 50, message = "{book-title-expected-size}")
    private String bookTitle;

    @Positive(message = "{author-must-be-selected}")
    private long authorId;

    @NotEmpty(message = "{genres-not-empty}")
    private Set<Long> genreIds;
}
