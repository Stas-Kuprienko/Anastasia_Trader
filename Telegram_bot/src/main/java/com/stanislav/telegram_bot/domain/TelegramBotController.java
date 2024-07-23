package com.stanislav.telegram_bot.domain;

import com.stanislav.telegram_bot.domain.elements.MainMenuCustomizer;
import com.stanislav.telegram_bot.domain.handler.CommandDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBotController extends TelegramLongPollingBot {

    private final String username;
    private final CommandDispatcher commandDispatcher;
    private final MainMenuCustomizer mainMenuCustomizer;


    @Autowired
    public TelegramBotController(CommandDispatcher commandDispatcher,
                                 MainMenuCustomizer mainMenuCustomizer,
                                 @Value("${telegram.username}") String username,
                                 @Value("${telegram.botToken}") String botToken) {

        super(botToken);
        this.username = username;
        this.commandDispatcher = commandDispatcher;
        this.mainMenuCustomizer = mainMenuCustomizer;
    }


    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                setMenuIfStart(update);
                execute(commandDispatcher.apply(update.getMessage()));
            }
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }


    private void setMenuIfStart(Update update) throws TelegramApiException {
        if (update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            if (text.equals("/start")) {
                executeAsync(mainMenuCustomizer
                        .form(update.getMessage().getFrom().getLanguageCode()));
            }
            System.out.println(update.getMessage().getText());
        }
    }
}