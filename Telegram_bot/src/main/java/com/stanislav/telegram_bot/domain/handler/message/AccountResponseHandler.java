package com.stanislav.telegram_bot.domain.handler.message;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component("/accounts")
public class AccountResponseHandler implements ResponseHandler {


    @Override
    public BotApiMethodMessage apply(Message message) {
        return new SendMessage();
    }
}
