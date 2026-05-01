package ru.otus.hw.rest.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import ru.otus.hw.rest.response.EntityUpdateResponseDto;

public class BadEntityResponseProvider {

    public static <T> ResponseEntity<EntityUpdateResponseDto<T>> getBadEntityResponse(BindingResult bindingResult) {
        var errors = bindingResult.getAllErrors()
                .stream()
                .map(it -> new EntityUpdateResponseDto.ErrorItem(it.getCodes()[0], it.getDefaultMessage()))
                .toList();

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new EntityUpdateResponseDto<T>(null, errors));
    }

}
