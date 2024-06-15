package com.stanislav.telegram_bot.domain.handler.message;

import com.stanislav.telegram_bot.domain.elements.KeyboardKit;
import com.stanislav.telegram_bot.domain.handler.ResponseHandler;
import com.stanislav.telegram_bot.domain.service.UserService;
import com.stanislav.telegram_bot.domain.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.Locale;

import static com.stanislav.telegram_bot.domain.handler.Commands.START;

@Component("/start")
public class StartResponseHandler implements ResponseHandler {

    private static final String resource = "http://localhost:8081/anastasia/";
    private final MessageSource messageSource;
    private final UserService userService;
    private final KeyboardKit keyboardKit;


    @Autowired
    public StartResponseHandler(MessageSource messageSource, UserService userService, KeyboardKit keyboardKit) {
        this.messageSource = messageSource;
        this.userService = userService;
        this.keyboardKit = keyboardKit;
    }


    @Override
    public BotApiMethodMessage handle(SessionContext context, Message message) {
        Long chatId = message.getChatId();
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        String lang = message.getFrom().getLanguageCode().toUpperCase();
        String name = message.getFrom().getFirstName();
        if (userService.findById(chatId).isEmpty()) {
            response.setText(messageSource.getMessage(
                    START.pattern
                            + '.' +
                            Response.greeting, new Object[]{name, resource}, Locale.of(lang)));
        } else {
            response.setReplyMarkup(keyboardKit.getMainKeyboard());
            response.setText(messageSource.getMessage(
                    START.pattern
                            + '.' + Response.forExistUser, new Object[]{name}, Locale.of(lang)));
        }
        return response;
    }

    enum Response {
        greeting, forExistUser
    }
}
