package com.stanislav.telegram_bot.domain.handler.message;

import com.stanislav.telegram_bot.domain.handler.Commands;
import com.stanislav.telegram_bot.domain.handler.ResponseHandler;
import com.stanislav.telegram_bot.domain.session.SessionContext;
import com.stanislav.telegram_bot.entities.user.ContextState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Locale;

@Component("default")
public class DefaultHandler implements ResponseHandler {

    private final MessageSource messageSource;

    @Autowired
    public DefaultHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @Override
    public BotApiMethodMessage handle(SessionContext context, Message message) {
        Long chatId = message.getChatId();
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        Locale locale = Locale.of(message.getFrom().getLanguageCode());
        if (context.getContextState() == ContextState.CLEAR) {
            String text = messageSource.getMessage(Commands.DEFAULT.pattern, null, locale);
            response.setText(text);
        } else {

        }
        return response;
    }
}