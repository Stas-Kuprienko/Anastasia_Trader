package com.stanislav.telegram_bot.domain.handler.message;

import com.stanislav.telegram_bot.domain.handler.Commands;
import com.stanislav.telegram_bot.domain.handler.ResponseHandler;
import com.stanislav.telegram_bot.domain.service.UserDataService;
import com.stanislav.telegram_bot.domain.session.SessionContext;
import com.stanislav.telegram_bot.entities.user.ContextState;
import com.stanislav.telegram_bot.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.Locale;

@Component("/reset")
public class ResetResponseHandler implements ResponseHandler {

    private final UserDataService userDataService;
    private final MessageSource messageSource;

    @Autowired
    public ResetResponseHandler(UserDataService userDataService, MessageSource messageSource) {
        this.userDataService = userDataService;
        this.messageSource = messageSource;
    }

    @Override
    public BotApiMethodMessage handle(SessionContext context, Message message) {
        context.reset();
        Long chatId = message.getChatId();
        User user = userDataService.findById(chatId).orElseThrow();
        user.setContextState(ContextState.CLEAR);
        String text = messageSource.getMessage(
                Commands.RESET.pattern, null, Locale.of(message.getFrom().getLanguageCode()));
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText(text);
        return response;
    }
}