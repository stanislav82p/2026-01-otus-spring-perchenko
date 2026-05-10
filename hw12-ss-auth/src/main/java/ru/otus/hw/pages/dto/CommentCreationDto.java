package ru.otus.hw.pages.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CommentCreationDto {


    @Positive(message = "{reader-must-be-selected}")
    private long readerId;

    @NotBlank(message = "{comment-text-not-blank}")
    @Size(min = 5, max = 500, message = "{comment-text-expected-size}")
    private String commentText;
}
