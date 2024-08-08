package com.anastasia.trade.controller.utils;

import com.anastasia.trade.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class MyExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<RestErrorResponse> notFoundHandle(Exception ex) {
        //TODO
        log.info(ex.getMessage());
        return ResponseEntity.status(404).body(new RestErrorResponse(ex.getMessage()));
    }
}