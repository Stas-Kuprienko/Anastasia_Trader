package com.stanislav.telegram_bot.domain.elements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MainMenuCustomizer {

    private static final String HELP = "/help";
    private static final String RESET = "/reset";
    private static final String USER = "/user";
    private static final String CURRENT = "/current";

    private final MessageSource messageSource;

    private final ConcurrentHashMap<String, SetMyCommands> commandsStorage;

    @Autowired
    public MainMenuCustomizer(MessageSource messageSource) {
        this.messageSource = messageSource;
        this.commandsStorage = new ConcurrentHashMap<>();
    }

    public SetMyCommands form(String lang) {
        SetMyCommands commands = commandsStorage.get(lang);
        if (commands == null) {
            Locale locale = Locale.of(lang);
            commands = new SetMyCommands();
            var commandList = List.of(
                    getCommand(HELP, locale),
                    getCommand(RESET, locale),
                    getCommand(CURRENT, locale),
                    getCommand(USER, locale)
            );
            commands.setCommands(commandList);
            commandsStorage.put(lang, commands);
        }
        return commands;
    }

    private BotCommand getCommand(String command, Locale locale) {
        String messageCode = "mainMenu.";
        String text = messageSource.getMessage(messageCode + command, null, locale);
        return new BotCommand(command, text);
    }
}