/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.telegram.domain.handler;

import com.anastasia.telegram.domain.session.SessionContext;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface ResponseHandler {

    BotApiMethodMessage handle(SessionContext context, Message message);
}
