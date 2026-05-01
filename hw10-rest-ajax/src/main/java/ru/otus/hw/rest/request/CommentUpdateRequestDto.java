package ru.otus.hw.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentUpdateRequestDto {

    @Positive(message = "{reader-must-be-provided}")
    private final long readerId;

    @NotBlank(message = "{comment-text-not-blank}")
    @Size(min = 5, max = 50, message = "{comment-text-expected-size}")
    private final String text;
}
