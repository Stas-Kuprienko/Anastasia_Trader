package com.stanislav.telegram_bot.domain;

import com.stanislav.telegram_bot.domain.elements.KeyboardKit;
import com.stanislav.telegram_bot.domain.handler.message.ResponseHandler;
import com.stanislav.telegram_bot.domain.service.UserService;
import com.stanislav.telegram_bot.domain.user_context.UserContext;
import com.stanislav.telegram_bot.domain.user_context.UserContextService;
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

    private final UserContextService userContextService;


    @Autowired
    public CommandDispatcher(ApplicationContext applicationContext,KeyboardKit keyboardKit,
                             UserService userService, UserContextService userContextService) {
        this.handlerMap = Arrays
                .stream(Commands.values())
                .collect(Collectors.toMap(c -> c.pattern,
                        c -> applicationContext.getBean(c.pattern, ResponseHandler.class)));
        this.keyboardKit = keyboardKit;
        this.userService = userService;
        this.userContextService = userContextService;
    }


    public BotApiMethodMessage apply(Message message) {
        Long chatId = message.getChatId();
        if (message.hasText()) {
            String text = message.getText();
            ResponseHandler handler = handlerMap.get(text);
            if (handler != null) {
                UserContext context = userContextService.get(chatId);
                return handler.handle(context, message);
            }
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(unknownCommand);
        return sendMessage;
    }
}