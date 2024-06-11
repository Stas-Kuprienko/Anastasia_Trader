package com.stanislav.telegram_bot.domain;

import com.stanislav.telegram_bot.domain.handler.message.MessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CommandDispatcher {

    private static final String unknownCommand = "Ты чё несёшь? Я тебя не понимаю.";

    private final Map<String, MessageHandler> handlerMap;

    public CommandDispatcher() {
        this.handlerMap = Arrays
                .stream(Commands.values())
                .collect(Collectors.toMap(c -> c.pattern, c -> c.handler));
    }

    public SendMessage handle(Message message) {
        Long chatId = message.getChatId();
        String text = message.getText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        MessageHandler handler = handlerMap.get(text);
        if (handler != null) {
            String response = handler.apply(chatId);
            sendMessage.setText(response);
            new SetMyCommands();
            sendMessage.setReplyMarkup(null);
            //TODO

        } else {
            sendMessage.setText(unknownCommand);
        }
        return sendMessage;
    }
}