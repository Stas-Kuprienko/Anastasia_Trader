package com.stanislav.telegram_bot.domain.handler.message;

import com.stanislav.telegram_bot.domain.handler.ResponseHandler;
import com.stanislav.telegram_bot.domain.session.SessionContext;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Configuration("/order")
public class OrderResponseHandler implements ResponseHandler {

    @Override
    public BotApiMethodMessage handle(SessionContext context, Message message) {
        return null;
    }
}
