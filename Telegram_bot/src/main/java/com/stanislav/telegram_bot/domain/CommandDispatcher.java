package com.stanislav.telegram_bot.domain;

import com.stanislav.telegram_bot.domain.handler.CommandHandler;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CommandDispatcher {

    private static final String unknownCommand = "Ты чё несёшь? Я тебя не понимаю.";

    private final Map<String, CommandHandler> handlerMap;

    public CommandDispatcher() {
        this.handlerMap = Arrays
                .stream(Command.values())
                .collect(Collectors.toMap(c -> c.pattern, c -> c.handler));
    }

    public String apply(Long chatId, String message) {
        CommandHandler handler = handlerMap.get(message);
        return handler != null ? handler.handle(chatId) : unknownCommand;
    }
}