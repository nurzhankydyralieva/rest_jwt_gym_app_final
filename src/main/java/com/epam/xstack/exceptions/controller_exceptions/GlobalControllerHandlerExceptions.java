package com.epam.xstack.exceptions.controller_exceptions;

import com.epam.xstack.exceptions.error_models.Error;
import com.epam.xstack.exceptions.error_models.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalControllerHandlerExceptions extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NullNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleNullNotAllowedException(NullNotAllowedException ex) {
        log.info("Null not allowed -  Exception: {}", ex.toString());
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder()
                .code(ex.getCodeStatus())
                .message(ex.getMessage())
                .build()).build(), ex.getHttpStatus());
    }
}
