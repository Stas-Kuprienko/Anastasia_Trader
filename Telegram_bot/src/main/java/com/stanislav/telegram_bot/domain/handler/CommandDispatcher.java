/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.telegram_bot.domain.handler;

import com.stanislav.telegram_bot.domain.elements.KeyboardKit;
import com.stanislav.telegram_bot.domain.service.UserService;
import com.stanislav.telegram_bot.domain.session.SessionContext;
import com.stanislav.telegram_bot.domain.session.SessionContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CommandDispatcher {

    private static final String unknownCommand = "Ты чё несёшь? Я тебя не понимаю.";

    private final Map<String, ResponseHandler> handlerMap;

    private final KeyboardKit keyboardKit;

    private final UserService userService;

    private final SessionContextService sessionContextService;


    @Autowired
    public CommandDispatcher(ApplicationContext applicationContext,KeyboardKit keyboardKit,
                             UserService userService, SessionContextService sessionContextService) {
        this.handlerMap = Arrays
                .stream(Commands.values())
                .collect(Collectors.toMap(c -> c.pattern,
                        c -> applicationContext.getBean(c.pattern, ResponseHandler.class)));
        this.keyboardKit = keyboardKit;
        this.userService = userService;
        this.sessionContextService = sessionContextService;
    }


    public BotApiMethodMessage apply(Message message) {
        Long chatId = message.getChatId();
        if (message.hasText()) {
            String text = message.getText();
            ResponseHandler handler = handlerMap.get(text);
            if (handler != null) {
                SessionContext context = sessionContextService.get(chatId);
                return handler.handle(context, message);
            }
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(unknownCommand);
        return sendMessage;
    }
}