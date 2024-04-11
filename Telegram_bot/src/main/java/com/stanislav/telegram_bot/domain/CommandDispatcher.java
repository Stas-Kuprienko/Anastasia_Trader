package com.stanislav.telegram_bot.domain;

import org.springframework.stereotype.Component;
import com.stanislav.telegram_bot.domain.handler.CommandHandler;
import com.stanislav.telegram_bot.domain.handler.HelpCommandHandler;
import com.stanislav.telegram_bot.domain.handler.StartCommandHandler;

@Component
public class CommandDispatcher {

    private static final String START = "/start";
    private static final String HELP = "/help";
    private static final String STOCKS = "/stocks";

    private static final String unknownCommand = "Ты чё несёшь? Я тебя не понимаю.";


    public String apply(String message) {

        CommandHandler commandHandler;
        switch (message) {
            case START -> commandHandler = new StartCommandHandler();
            case HELP -> commandHandler = new HelpCommandHandler();

            default -> {
                return unknownCommand;
            }
        }

        return commandHandler.getResponse();
    }
}
