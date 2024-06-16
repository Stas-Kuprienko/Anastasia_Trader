package com.stanislav.telegram_bot.rest;

import com.stanislav.telegram_bot.domain.TelegramBotController;
import com.stanislav.telegram_bot.domain.service.UserService;
import com.stanislav.telegram_bot.entities.user.Account;
import com.stanislav.telegram_bot.entities.user.User;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final RestTemplate restTemplate;
    private final String resource;
    private final TelegramBotController controller;
    private final MessageSource messageSource;


    @Autowired
    public UserController(UserService userService, RestTemplate restTemplate, TelegramBotController controller,
                          @Value("${api.resource}") String resource, MessageSource messageSource) {
        this.userService = userService;
        this.restTemplate = restTemplate;
        this.resource = resource;
        this.controller = controller;
        this.messageSource = messageSource;
    }


    @PostMapping("/register")
    public boolean register(@RequestParam("login") String login,
                            @RequestParam("chatId") long chatId,
                            @RequestParam("name") String name) {

        try {
            User user = new User(chatId, login, name);
            String uri = resource + Mapping.ACCOUNTS.v + "?login=" + login;
            ResponseEntity<Account[]> response =
                    restTemplate.getForEntity(uri, Account[].class);
            List<Account> accounts = List.of(Objects.requireNonNull(response.getBody()));
            user.setAccounts(accounts);
            //TODO locale
            String messageToUser = messageSource.getMessage("signed-up", null, Locale.of("RU"));
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(messageToUser);
            controller.execute(sendMessage);
            userService.save(user);
            return true;
        } catch (RestClientException | NullPointerException | PersistenceException e) {
            //TODO logs
            return false;
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    enum Mapping {
        ACCOUNTS("/accounts");

        final String v;

        Mapping(String v) {
            this.v = v;
        }
    }
}
