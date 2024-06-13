package com.stanislav.telegram_bot.domain.handler.message;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface ResponseHandler {

    BotApiMethodMessage apply(Message message);
}
