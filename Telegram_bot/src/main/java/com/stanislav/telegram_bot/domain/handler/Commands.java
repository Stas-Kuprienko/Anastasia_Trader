/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.telegram_bot.domain.handler;

public enum Commands {

    START("/start"),
    HELP("/help"),
    RESET("/reset"),
    ACCOUNT("/accounts"),
    ORDER("/order"),
    UNRECOGNIZED("unrecognized"),
    ;

    public final String pattern;

    Commands(String pattern) {
        this.pattern = pattern;
    }
}
