package com.anastasia.telegram.domain.handler.message;

import com.anastasia.telegram.domain.handler.Commands;
import com.anastasia.telegram.domain.handler.ResponseHandler;
import com.anastasia.telegram.domain.service.UserService;
import com.anastasia.telegram.domain.session.SessionContext;
import com.anastasia.telegram.entities.user.ContextState;
import com.anastasia.telegram.entities.user.UserChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.Locale;

@Component("/reset")
public class ResetResponseHandler implements ResponseHandler {

    private final UserService userService;
    private final MessageSource messageSource;

    @Autowired
    public ResetResponseHandler(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @Override
    public BotApiMethodMessage handle(SessionContext context, Message message) {
        context.reset();
        Long chatId = message.getChatId();
        UserChat userChat = userService.findByChatId(chatId);
        userChat.setContextState(ContextState.CLEAR);
        String text = messageSource.getMessage(
                Commands.RESET.pattern, null, Locale.of(message.getFrom().getLanguageCode()));
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText(text);
        return response;
    }
}