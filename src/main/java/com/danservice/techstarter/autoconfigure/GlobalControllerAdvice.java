package com.danservice.techstarter.autoconfigure;

import jakarta.validation.ValidationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.ErrorResponse.create;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({ValidationException.class, IllegalArgumentException.class})
    public ErrorResponse badRequest(Exception exception) {
        return create(exception, BAD_REQUEST, exception.getMessage());
    }

}
