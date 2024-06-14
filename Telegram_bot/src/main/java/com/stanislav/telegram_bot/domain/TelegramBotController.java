package com.stanislav.telegram_bot.domain;

import com.stanislav.telegram_bot.domain.handler.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBotController extends TelegramLongPollingBot {

    private final String username;
    private final CommandDispatcher commandDispatcher;


    public TelegramBotController(@Value("${telegram.username}") String username,
                                 @Value("${telegram.botToken}") String botToken,
                                 @Autowired CommandDispatcher commandDispatcher) {

        super(botToken);
        this.username = username;
        this.commandDispatcher = commandDispatcher;
    }


    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                if (update.getMessage().hasText()) {
                    System.out.println(update.getMessage().getText());
                }
                execute(commandDispatcher.apply(update.getMessage()));
            }
        } catch (TelegramApiException e) {
            //TODO logs
            throw new RuntimeException(e);
        }
    }
}