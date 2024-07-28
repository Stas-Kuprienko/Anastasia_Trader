package com.stanislav.ui.controller.service;

import com.stanislav.ui.model.ResponseError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.Locale;

@Slf4j
@ControllerAdvice
public class MyExceptionHandler extends ResponseEntityExceptionHandler {

    //TODO . NOT WORKING, NEEDED TO FIX!

    private final MessageSource messageSource;

    @Autowired
    public MyExceptionHandler(@Qualifier("errorMessageSource") MessageSource messageSource) {
        this.messageSource = messageSource;
        setMessageSource(messageSource);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception e, WebRequest request) throws Exception {
        log.info(e.getMessage());
        ResponseError error = new ResponseError(messageSource.getMessage(ErrorCase.NOT_FOUND.toString(), null, Locale.of("RU")), 404);
        request.setAttribute("error", error, WebRequest.SCOPE_REQUEST);
        return handleException(e, request);
    }
}