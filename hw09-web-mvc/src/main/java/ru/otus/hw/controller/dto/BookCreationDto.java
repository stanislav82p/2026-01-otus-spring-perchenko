package ru.otus.hw.controller.dto;

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

    @Positive(message = "{author-must-be-selected}")
    private long authorId;

    @NotBlank(message = "{book-title-not-blank}")
    @Size(min = 5, max = 50, message = "{book-title-expected-size}")
    private String bookTitle;

    @NotEmpty(message = "{genres-not-empty}")
    private Set<Long> genreIds;
}
