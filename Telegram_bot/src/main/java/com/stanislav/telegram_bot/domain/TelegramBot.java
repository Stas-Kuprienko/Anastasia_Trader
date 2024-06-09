package com.stanislav.telegram_bot.domain;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String username;

    private final CommandDispatcher commandDispatcher;


    public TelegramBot(@Value("${telegram.username}") String username,
                       @Value("${telegram.botToken}") String botToken,
                       @Autowired CommandDispatcher commandDispatcher) {
        super(botToken);
        this.username = username;
        this.commandDispatcher = commandDispatcher;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().getText());
        if (update.hasMessage()) {

            Message message = update.getMessage();
            String command = message.getText();
            Long chatId = message.getChatId();
            String response;

            if(command != null) {
                response = commandDispatcher.apply(chatId, command);
                send(chatId, response);
            }
        }
    }


    private void send(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        try {
            executeAsync(sendMessage);
        } catch (TelegramApiException e) {
            //TODO
            e.printStackTrace();
        }
    }
}