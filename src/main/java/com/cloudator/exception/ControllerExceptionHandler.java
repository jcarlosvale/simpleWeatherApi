package com.cloudator.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class ControllerExceptionHandler {

    @ExceptionHandler(LoadMonitoredLocationsException.class)
    public ResponseEntity<String> handleLoadToLocationsToMonitorException(LoadMonitoredLocationsException re) {
        log.error(re.getMessage());
        log.debug(re.getStackTrace());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException re) {
        log.error(re.getMessage());
        log.debug(re.getStackTrace());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
