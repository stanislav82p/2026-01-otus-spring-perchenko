package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.exceptions.BadHttpRequestParamsException;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.rest.response.ErrorResponseDto;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handeNotFoundException(EntityNotFoundException ex) {
        var body = new ErrorResponseDto(ex.getMessage(), ex.getUserMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(BadHttpRequestParamsException.class)
    public ResponseEntity<ErrorResponseDto> handeBadRequestParamsException(BadHttpRequestParamsException ex) {
        var body = new ErrorResponseDto(ex.getMessage(), ex.getUserMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(body);
    }
}
