package com.anastasia.telegram.domain.handler.message;

import com.anastasia.telegram.domain.elements.KeyboardKit;
import com.anastasia.telegram.domain.handler.Commands;
import com.anastasia.telegram.domain.handler.ResponseHandler;
import com.anastasia.telegram.domain.service.UserService;
import com.anastasia.telegram.domain.session.SessionContext;
import com.anastasia.telegram.entities.user.Account;
import com.anastasia.telegram.entities.user.UserChat;
import com.anastasia.telegram.rest.consumers.UserDataRestConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Locale;

@Component("/accounts")
public class AccountResponseHandler implements ResponseHandler {

    private final KeyboardKit keyboardKit;
    private final UserService userService;
    private final UserDataRestConsumer userDataRestConsumer;
    private final MessageSource messageSource;

    @Autowired
    public AccountResponseHandler(KeyboardKit keyboardKit, UserService userService, UserDataRestConsumer userDataRestConsumer, MessageSource messageSource) {
        this.keyboardKit = keyboardKit;
        this.userService = userService;
        this.userDataRestConsumer = userDataRestConsumer;
        this.messageSource = messageSource;
    }


    @Override
    public BotApiMethodMessage handle(SessionContext context, Message message) {
        Long chatId = message.getChatId();
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        Locale locale = Locale.of(message.getFrom().getLanguageCode());
        UserChat userChat = userService.findByChatId(chatId);
        List<Account> accountList = userDataRestConsumer.getAccounts(userChat.getUser().getId());
        var accountsBoard = keyboardKit.accounts(accountList);
        response.setReplyMarkup(accountsBoard);
        String text = messageSource.getMessage(Commands.ACCOUNT.pattern, null, locale);
        response.setText(text);
        return response;
    }
}