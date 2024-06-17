package com.stanislav.trade.web.controller;

import org.springframework.stereotype.Service;

@Service
public class ErrorDispatcher {

    public String apply(int statusCode) {
        return "/";
    }

    public String apply(Case aCase) {
        return "/";
    }


    public enum Case {

        TELEGRAM_ID_LOST
    }
}
