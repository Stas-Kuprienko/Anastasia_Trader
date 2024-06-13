package com.stanislav.telegram_bot.domain;

public enum Commands {

    START("/start"),
    HELP("/help"),
    ACCOUNT("/accounts"),
    ORDER("/order")
    ;

    public final String pattern;

    Commands(String pattern) {
        this.pattern = pattern;
    }
}
