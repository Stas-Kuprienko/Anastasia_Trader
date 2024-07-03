package com.stanislav.trade.web.controller.service;

import com.stanislav.trade.web.service.ErrorCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.jasper.JasperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Arrays;
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

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handle404(HttpServletRequest request, NoHandlerFoundException e) {
        log.info(e.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(ErrorController.ERROR_PAGE);
        modelAndView.addObject("error", new ErrorController.ErrorModel(
                messageSource.getMessage(ErrorCase.NOT_FOUND.toString(), null, Locale.of("RU")),
                "not-found.webp",
                404));
        return modelAndView;
    }

    @ExceptionHandler(JasperException.class)
    public ModelAndView handleJasperException(Exception e) {
        log.error(Arrays.toString(e.getStackTrace()));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(ErrorController.ERROR_PAGE);
        ErrorController.ErrorModel errorModel = new ErrorController.ErrorModel(
                messageSource.getMessage("500", null, Locale.of("RU")),
                ErrorController.Images.SERVER_ERROR.file, 500);
        modelAndView.addObject(ErrorController.ERROR_KEY, errorModel);
        return modelAndView;
    }
}