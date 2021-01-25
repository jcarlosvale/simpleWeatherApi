package com.cloudator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(LoadToLocationsToMonitorException.class)
    public ResponseEntity<String> handleLoadToLocationsToMonitorException(RuntimeException re) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
