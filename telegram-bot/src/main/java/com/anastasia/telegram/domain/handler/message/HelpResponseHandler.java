package com.anastasia.telegram.domain.handler.message;

import com.anastasia.telegram.domain.handler.Commands;
import com.anastasia.telegram.domain.handler.ResponseHandler;
import com.anastasia.telegram.domain.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Locale;

@Component("/help")
public class HelpResponseHandler implements ResponseHandler {

    private static final String manual = """
            Для работы с ботом вы должны быть зарегистрированы на сайте Anastasia-Trade.com.
            
            """;

    private final MessageSource messageSource;


    @Autowired
    public HelpResponseHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @Override
    public BotApiMethodMessage handle(SessionContext context, Message message) {
        SendMessage sendMessage = new SendMessage();
        String lang = message.getFrom().getLanguageCode().toUpperCase();
        sendMessage.setChatId(message.getChatId());
        String response = messageSource.getMessage(Commands.HELP.pattern, null, Locale.of(lang));
        sendMessage.setText(response);
        return sendMessage;
    }
}
