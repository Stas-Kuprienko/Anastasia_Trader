package com.stanislav.telegram_bot.rest.controllers;

import com.stanislav.telegram_bot.domain.TelegramBotController;
import com.stanislav.telegram_bot.domain.service.UserDataService;
import com.stanislav.telegram_bot.entities.user.ContextState;
import com.stanislav.telegram_bot.entities.user.User;
import com.stanislav.telegram_bot.entities.user.UserChat;
import com.stanislav.telegram_bot.exceptions.NotFoundException;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.Locale;

@RestController
@RequestMapping("/user")
public class UserDataRestController {

    private final UserDataService userDataService;
    private final TelegramBotController controller;
    private final MessageSource messageSource;


    @Autowired
    public UserDataRestController(UserDataService userDataService, TelegramBotController controller, MessageSource messageSource) {
        this.userDataService = userDataService;
        this.controller = controller;
        this.messageSource = messageSource;
    }


    @PostMapping("/register")
    public boolean register(@RequestParam("chatId") long chatId,
                            @RequestParam("locale") String locale,
                            @RequestBody User user) {
        try {
            UserChat userChat = new UserChat(chatId, user, ContextState.CLEAR);
            userDataService.save(userChat);
            String messageToUser = messageSource.getMessage("signed-up", null, Locale.of(locale));
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(messageToUser);
            controller.execute(sendMessage);
            return true;
        } catch (RestClientException | PersistenceException | NotFoundException e) {
            //TODO logs
            return false;
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
