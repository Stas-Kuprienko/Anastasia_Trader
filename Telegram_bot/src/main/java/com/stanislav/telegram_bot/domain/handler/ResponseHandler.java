/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.telegram_bot.domain.handler;

import com.stanislav.telegram_bot.domain.session.SessionContext;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface ResponseHandler {

    BotApiMethodMessage handle(SessionContext context, Message message);
}
