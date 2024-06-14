package com.stanislav.telegram_bot.domain.handler.message;

import com.stanislav.telegram_bot.domain.user_context.UserContext;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface ResponseHandler {

    BotApiMethodMessage handle(UserContext context, Message message);
}
