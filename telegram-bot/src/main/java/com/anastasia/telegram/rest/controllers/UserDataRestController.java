package com.anastasia.telegram.rest.controllers;

import com.anastasia.telegram.domain.TelegramBotController;
import com.anastasia.telegram.domain.service.UserService;
import com.anastasia.telegram.entities.user.ContextState;
import com.anastasia.telegram.entities.user.User;
import com.anastasia.telegram.entities.user.UserChat;
import com.anastasia.telegram.exceptions.NotFoundException;
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

    private final UserService userService;
    private final TelegramBotController controller;
    private final MessageSource messageSource;


    @Autowired
    public UserDataRestController(UserService userService, TelegramBotController controller, MessageSource messageSource) {
        this.userService = userService;
        this.controller = controller;
        this.messageSource = messageSource;
    }


    @PostMapping("/register")
    public boolean register(@RequestParam("chatId") long chatId,
                            @RequestParam("locale") String locale,
                            @RequestBody User user) {
        try {
            UserChat userChat = new UserChat(chatId, user, ContextState.CLEAR);
            userService.save(userChat);
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
