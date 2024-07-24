package com.stanislav.telegram_bot.domain.handler.message;

import com.stanislav.telegram_bot.domain.elements.KeyboardKit;
import com.stanislav.telegram_bot.domain.handler.Commands;
import com.stanislav.telegram_bot.domain.handler.ResponseHandler;
import com.stanislav.telegram_bot.domain.service.UserService;
import com.stanislav.telegram_bot.domain.session.SessionContext;
import com.stanislav.telegram_bot.entities.user.Account;
import com.stanislav.telegram_bot.entities.user.UserChat;
import com.stanislav.telegram_bot.rest.consumers.UserDataRestConsumer;
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