/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.telegram.domain.handler;

public enum Commands {

    START("/start"),
    HELP("/help"),
    RESET("/reset"),
    ACCOUNT("/accounts"),
    ORDER("/order"),
    DEFAULT("default"),
    ;

    public final String pattern;

    Commands(String pattern) {
        this.pattern = pattern;
    }
}
