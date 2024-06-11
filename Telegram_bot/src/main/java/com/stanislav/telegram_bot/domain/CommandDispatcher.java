package com.stanislav.telegram_bot.domain;

import com.stanislav.telegram_bot.datasource.repositories.UserDao;
import com.stanislav.telegram_bot.domain.elements.KeyboardKit;
import com.stanislav.telegram_bot.domain.handler.message.MessageHandler;
import com.stanislav.telegram_bot.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CommandDispatcher {

    private static final String unknownCommand = "Ты чё несёшь? Я тебя не понимаю.";

    private final Map<String, MessageHandler> handlerMap;

    @Autowired
    private KeyboardKit keyboardKit;

    @Autowired
    UserDao userDao;

    @Autowired
    public CommandDispatcher(ApplicationContext applicationContext) {
        this.handlerMap = Arrays
                .stream(Commands.values())
                .collect(Collectors.toMap(c -> c.pattern,
                        c -> applicationContext.getBean(c.pattern, MessageHandler.class)));
    }

    public SendMessage handle(Message message) {
        Long chatId = message.getChatId();
        String text = " ";
        if (message.hasText()) {
            text = message.getText();
        }
        System.out.println(message);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        MessageHandler handler = handlerMap.get(text);
        if (handler != null) {
            String response = handler.apply(chatId);
            sendMessage.setText(response);
            User user = userDao.findById(1L).orElseThrow();
            var button = keyboardKit.accounts(user.getAccounts());
//            new SetMyCommands();
//            sendMessage.setReplyMarkup(null);
            sendMessage.setReplyMarkup(button);
            //TODO

        } else {
            sendMessage.setText(unknownCommand);
        }
        return sendMessage;
    }
}