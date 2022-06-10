package com.igorivkin.testprogramrunner.api;

import com.igorivkin.testprogramrunner.api.dto.container.ErrorResponse;
import com.igorivkin.testprogramrunner.exception.RunningTooLongException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RunningTooLongException.class)
    public ErrorResponse handleRunningTooLongException(RunningTooLongException ex) {
        return ErrorResponse.builder()
                .message("Your application runs too long")
                .build();
    }
}
