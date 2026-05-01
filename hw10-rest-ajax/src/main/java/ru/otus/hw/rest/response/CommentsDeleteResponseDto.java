package ru.otus.hw.rest.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentsDeleteResponseDto {

    private final int nDeleted;
}
