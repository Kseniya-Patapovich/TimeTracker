package com.timetracker.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerGlobal {
    @ExceptionHandler(value = SameUserInDatabase.class)
    public ResponseEntity<HttpStatusCode> sameUserInDatabase(Exception exception) {
        log.error(String.valueOf(exception));
        return new ResponseEntity<>(HttpStatus.valueOf(409));
    }
}
