package com.anastasia.telegram.domain.handler.message;

import com.anastasia.telegram.domain.session.SessionContext;
import com.anastasia.telegram.domain.handler.ResponseHandler;
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
