package com.stanislav.telegram_bot.domain.handler.message;

import com.stanislav.telegram_bot.domain.elements.KeyboardKit;
import com.stanislav.telegram_bot.domain.handler.Commands;
import com.stanislav.telegram_bot.domain.handler.ResponseHandler;
import com.stanislav.telegram_bot.domain.service.UserService;
import com.stanislav.telegram_bot.domain.session.SessionContext;
import io.jsonwebtoken.JwtBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.Locale;

@Component("/start")
public class StartResponseHandler implements ResponseHandler {

    private static final String AUTH = "telegram-bot/auth";
    private static final String CHAT_ID = "chatId";
    private final String resource;
    private final MessageSource messageSource;
    private final UserService userService;
    private final KeyboardKit keyboardKit;
    private final JwtBuilder jwtBuilder;


    @Autowired
    public StartResponseHandler(MessageSource messageSource, UserService userService,
                                KeyboardKit keyboardKit, JwtBuilder jwtBuilder,
                                @Value("${api.resource}") String resource) {
        this.messageSource = messageSource;
        this.userService = userService;
        this.keyboardKit = keyboardKit;
        this.jwtBuilder = jwtBuilder;
        this.resource = resource;
    }


    @Override
    public BotApiMethodMessage handle(SessionContext context, Message message) {
        Long chatId = message.getChatId();
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        String lang = message.getFrom().getLanguageCode().toUpperCase();
        String name = message.getFrom().getFirstName();
        if (context == null || userService.findById(chatId).isEmpty()) {
            String chatIdToken = jwtBuilder.claim("chatId", chatId).compact();
            String link = resource + AUTH + '?' +
                    CHAT_ID + '=' + chatId + '&' + 't' + '=' + chatIdToken;
//            response.setReplyMarkup(keyboardKit.signUpLink("Click", link));
            response.setText(messageSource.getMessage(
                    Commands.START.pattern
                            + '.' +
                            Response.greeting, new Object[]{name}, Locale.of(lang)));
        } else {

            response.setReplyMarkup(keyboardKit.getMainKeyboard());
            response.setText(messageSource.getMessage(
                    Commands.START.pattern
                            + '.' + Response.forExistUser, new Object[]{name}, Locale.of(lang)));
        }
        return response;
    }

    enum Response {
        greeting, forExistUser
    }
}
