/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.telegram_bot.domain.handler;

import com.stanislav.telegram_bot.domain.service.UserDataService;
import com.stanislav.telegram_bot.domain.session.SessionContext;
import com.stanislav.telegram_bot.domain.session.SessionContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CommandDispatcher {

    private static final String UNKNOWN = "unknown";

    private final Map<String, ResponseHandler> handlerMap;
    private final UserDataService userDataService;
    private final SessionContextService sessionContextService;
    private final MessageSource messageSource;


    @Autowired
    public CommandDispatcher(ApplicationContext applicationContext, UserDataService userDataService,
                             SessionContextService sessionContextService, MessageSource messageSource) {
        this.handlerMap = Arrays
                .stream(Commands.values())
                .collect(Collectors.toMap(c -> c.pattern,
                        c -> applicationContext.getBean(c.pattern, ResponseHandler.class)));
        this.userDataService = userDataService;
        this.sessionContextService = sessionContextService;
        this.messageSource = messageSource;
    }


    public BotApiMethodMessage apply(Message message) {
        Long chatId = message.getChatId();
        if (message.hasText()) {
            String text = message.getText();
            if (userDataService.findById(chatId).isEmpty() && !text.equals("/start")) {
                return handlerMap.get("/start").handle(null, message);
            }
            ResponseHandler handler = handlerMap.get(text);
            SessionContext context = sessionContextService.get(chatId);
            if (handler == null) {
                handler = handlerMap.get(Commands.UNRECOGNIZED.pattern);
            }
            return handler.handle(context, message);
        }
        SendMessage sendMessage = new SendMessage();
        String unknownCommand = messageSource
                .getMessage(UNKNOWN, null, Locale.of(message.getFrom().getLanguageCode()));
        sendMessage.setChatId(chatId);
        sendMessage.setText(unknownCommand);
        return sendMessage;
    }
}