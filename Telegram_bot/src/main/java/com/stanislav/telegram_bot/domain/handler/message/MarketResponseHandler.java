package com.stanislav.telegram_bot.domain.handler.message;

import com.stanislav.telegram_bot.domain.elements.KeyboardKit;
import com.stanislav.telegram_bot.domain.handler.ResponseHandler;
import com.stanislav.telegram_bot.domain.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class MarketResponseHandler implements ResponseHandler {

    @Autowired
    private KeyboardKit keyboardKit;

    @Override
    public BotApiMethodMessage handle(SessionContext context, Message message) {
        return null;
    }
}
