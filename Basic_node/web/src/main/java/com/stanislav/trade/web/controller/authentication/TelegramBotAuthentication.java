package com.stanislav.trade.web.controller.authentication;

import com.stanislav.trade.datasource.UserDao;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.controller.ErrorDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramBotAuthentication {

    private final String telegramBot;
    private final String telegramBotApiUrl;

    private final RestTemplate restTemplate;
    private final UserDao userDao;
    private final ErrorDispatcher errorDispatcher;

    @Autowired
    public TelegramBotAuthentication(RestTemplate restTemplate, ErrorDispatcher errorDispatcher, UserDao userDao,
                                     @Value("${telegram.rest}") String telegramBotApiUrl,
                                     @Value("${telegram_bot.link}") String telegramBot) {
        this.telegramBotApiUrl = telegramBotApiUrl;
        this.telegramBot = telegramBot;
        this.restTemplate = restTemplate;
        this.userDao = userDao;
        this.errorDispatcher = errorDispatcher;
    }

    public String signUpToTelegram(Long chatId, User user) {
        //TODO
        if (confirmChatId(chatId) != null) {
            boolean response = requestSignUpToTelegram(chatId, user);
            if (response && userDao.addTelegramChatId(user, chatId)) {
                return "redirect:" + telegramBot;
            } else {
                //TODO
                return errorDispatcher.apply(500);
            }
        } else {
            return errorDispatcher.apply(400);
        }
    }

    public boolean requestSignUpToTelegram(Long chatId, User user) {
        MultiValueMap<String, Object> parameterMap = new LinkedMultiValueMap<>();
        parameterMap.add(AuthenticationController.Args.login.toString(), user.getLogin());
        parameterMap.add(AuthenticationController.Args.chatId.toString(), chatId);
        parameterMap.add(AuthenticationController.Args.name.toString(), user.getName());
        HttpEntity<MultiValueMap<String, Object>> parameters = new HttpEntity<>(parameterMap);
        //TODO remake with json response
        ResponseEntity<Boolean> response = restTemplate
                .exchange(telegramBotApiUrl + "user/register", HttpMethod.POST, parameters, Boolean.class);
        return Boolean.TRUE.equals(response.getBody());
    }

    public Integer confirmChatId(long chatId) {
        MultiValueMap<String, Object> parameterMap = new LinkedMultiValueMap<>();
        parameterMap.add("chatId", chatId);
        HttpEntity<MultiValueMap<String, Object>> parameters = new HttpEntity<>(parameterMap);
        ResponseEntity<Integer> response = restTemplate
                .exchange(telegramBotApiUrl + "user/confirm", HttpMethod.GET, parameters, Integer.class);
        return response.getBody();
    }
}
