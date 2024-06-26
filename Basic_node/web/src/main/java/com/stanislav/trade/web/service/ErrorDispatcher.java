package com.stanislav.trade.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ErrorDispatcher {

    public static final String ERROR_PAGE = "error_page";

    private final MessageSource messageSource;

    @Autowired
    public ErrorDispatcher(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    public String apply(int statusCode) {
        return "/";
    }

    public String apply(ErrorCase c) {
        //TODO
        return messageSource.getMessage(c.name(), null, Locale.of("RU"));
    }


}