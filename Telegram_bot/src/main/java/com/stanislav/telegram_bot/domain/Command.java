package com.stanislav.telegram_bot.domain;

import com.stanislav.telegram_bot.domain.handler.CommandHandler;
import com.stanislav.telegram_bot.domain.handler.HelpCommandHandler;
import com.stanislav.telegram_bot.domain.handler.StartCommandHandler;

public enum Command {

    START("/start", new StartCommandHandler()),
    HELP("/help", new HelpCommandHandler()),
    ;

    public final String pattern;
    public final CommandHandler handler;

    Command(String pattern, CommandHandler handler) {
        this.pattern = pattern;
        this.handler = handler;
    }
}
