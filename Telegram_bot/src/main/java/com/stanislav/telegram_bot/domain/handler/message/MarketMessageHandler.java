package com.stanislav.telegram_bot.domain.handler.message;

import com.stanislav.telegram_bot.domain.elements.KeyboardKit;
import org.springframework.beans.factory.annotation.Autowired;

public class MarketMessageHandler implements MessageHandler {

    @Autowired
    private KeyboardKit keyboardKit;

    @Override
    public String apply(Long chatId) {
        return null;
    }
}
