package ru.otus.hw.rest.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponseDto {

    private final String debugMessage;

    private final String userMessage;
}
