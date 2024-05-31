package com.danservice.techstarter.autoconfigure;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.ErrorResponse.create;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({ValidationException.class, IllegalArgumentException.class})
    public ErrorResponse badRequest(Exception exception) {
        log.info("Returning 400 Bad Request", exception);
        return create(exception, BAD_REQUEST, exception.getMessage());
    }

}
