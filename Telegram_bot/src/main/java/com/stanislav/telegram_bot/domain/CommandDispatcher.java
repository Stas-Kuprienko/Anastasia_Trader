package com.stanislav.telegram_bot.domain;

import com.stanislav.telegram_bot.domain.elements.KeyboardKit;
import com.stanislav.telegram_bot.domain.handler.message.ResponseHandler;
import com.stanislav.telegram_bot.domain.service.UserService;
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

    @Autowired
    private KeyboardKit keyboardKit;

    @Autowired
    UserService userService;


    @Autowired
    public CommandDispatcher(ApplicationContext applicationContext) {
        this.handlerMap = Arrays
                .stream(Commands.values())
                .collect(Collectors.toMap(c -> c.pattern,
                        c -> applicationContext.getBean(c.pattern, ResponseHandler.class)));
    }


    public BotApiMethodMessage handle(Message message) {
        Long chatId = message.getChatId();
        SendMessage sendMessage;
        if (message.hasText()) {
            String text = message.getText();
            ResponseHandler handler = handlerMap.get(text);
            if (handler != null) {
                sendMessage = (SendMessage) handler.apply(message);
//            new SetMyCommands();
//            sendMessage.setReplyMarkup(null);
                //TODO

            } else {
                sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText(unknownCommand);
            }
        } else {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(unknownCommand);
        }
        return sendMessage;
    }
}