package com.stanislav.telegram_bot.domain;

public enum Commands {

    START("/start"),
    HELP("/help"),
    ACCOUNT("/accounts")
    ;

    public final String pattern;

    Commands(String pattern) {
        this.pattern = pattern;
    }
}
