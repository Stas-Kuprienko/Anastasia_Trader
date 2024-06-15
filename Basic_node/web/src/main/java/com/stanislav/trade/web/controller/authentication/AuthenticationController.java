package com.stanislav.trade.web.controller.authentication;

import com.stanislav.trade.datasource.UserDao;
import com.stanislav.trade.entities.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
public class AuthenticationController {

    private final String LOGIN_PAGE = "login";
    private final String SIGN_UP_PAGE = "sign-up";

    private final String telegramBotUrl;
    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;
    private final RestTemplate restTemplate;


    @Autowired
    public AuthenticationController(PasswordEncoder passwordEncoder, UserDao userDao, RestTemplate restTemplate,
                                    @Value("${telegram.resource}") String telegramBotUrl) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
        this.restTemplate = restTemplate;
        this.telegramBotUrl = telegramBotUrl;
    }


    @GetMapping("/sign-up")
    public String signUp() {
        return SIGN_UP_PAGE;
    }

    @GetMapping("/login")
    public String login() {
        return LOGIN_PAGE;
    }

    @PostMapping("/sign-up")
    public String signUpHandle(@RequestParam(name = "email") String login,
                               @RequestParam(name = "password") String password,
                               @RequestParam(name = "name", required = false) String name,
                               @RequestParam(name = "chatId", required = false) Long chatId,
                               Model model) {

        System.out.println(chatId);
        User user = new User(
                login,
                passwordEncoder.encode(password),
                User.Role.USER,
                name != null ? name : "",
                chatId);
        userDao.save(user);
        if (sendUserToTelegram(login, chatId, name)) {
            model.addAttribute("email", login);
            model.addAttribute("password", password);
            return LOGIN_PAGE;
        } else {
            throw new HttpServerErrorException(HttpStatusCode.valueOf(500));
        }
    }

    @GetMapping("/telegram/sign-up")
    public String loginFromTelegram(@RequestParam("chatId") String chatId, HttpServletRequest request) {
        request.setAttribute("chatId", chatId);
        return SIGN_UP_PAGE;
    }


    private boolean sendUserToTelegram(String login, Long chatId, String name) {
        MultiValueMap<String, Object> parameterMap = new LinkedMultiValueMap<>();
        parameterMap.add("login", login);
        parameterMap.add("chatId", chatId);
        parameterMap.add("name", name);
        HttpEntity<MultiValueMap<String, Object>> parameters = new HttpEntity<>(parameterMap);
        ResponseEntity<Boolean> response = restTemplate
                .exchange(telegramBotUrl, HttpMethod.POST, parameters, Boolean.class);
        return Boolean.TRUE.equals(response.getBody());
    }
}
