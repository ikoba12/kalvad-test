package com.kalvad.test.controller;

import com.kalvad.test.model.bean.response.GeneralErrorResponse;
import com.kalvad.test.model.enums.ErrorCode;
import com.kalvad.test.model.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerControllerAdvice {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> traderException(ApiException e) {
        log.error("Error", e);
        return new ResponseEntity<>(
                new GeneralErrorResponse(e.getErrorCode(), e.getErrorMessage()),
                e.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(MethodArgumentNotValidException ex) {

        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        String errorMessage = errors.stream().map(Object::toString).collect(Collectors.joining(", "));
        return new ResponseEntity<>(new GeneralErrorResponse(ErrorCode.INVALID_REQUEST.getCode(), errorMessage), HttpStatus.BAD_REQUEST);
    }

}
