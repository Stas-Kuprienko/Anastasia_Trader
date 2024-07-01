package com.stanislav.trade.web.controller.service;

import com.stanislav.trade.web.service.ErrorCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import java.util.Locale;

@Slf4j
@ControllerAdvice
public class MyExceptionHandler {

    //TODO . NOT WORKING, NEEDED TO FIX!

    private final MessageSource messageSource;

    @Autowired
    public MyExceptionHandler(@Qualifier("errorMessageSource") MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handle(HttpServletRequest request, NoHandlerFoundException e) {
        log.info(e.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error_page");
        modelAndView.addObject("error", new ErrorController.ErrorModel(
                messageSource.getMessage(ErrorCase.NOT_FOUND.toString(), null, Locale.of("RU")),
                "not-found.webp",
                404));
        return modelAndView;
    }

}