package com.stanislav.telegram_bot.domain;

import com.stanislav.telegram_bot.domain.handler.message.MessageHandler;
import com.stanislav.telegram_bot.domain.handler.message.HelpMessageHandler;
import com.stanislav.telegram_bot.domain.handler.message.StartMessageHandler;

public enum Commands {

    START("/start", new StartMessageHandler()),
    HELP("/help", new HelpMessageHandler()),
    ;

    public final String pattern;
    public final MessageHandler handler;

    Commands(String pattern, MessageHandler handler) {
        this.pattern = pattern;
        this.handler = handler;
    }
}
