package com.stanislav.trade.web.controller.authentication;

import com.stanislav.trade.datasource.UserDao;
import com.stanislav.trade.entities.user.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class AuthenticationController {

    private final static String LOGIN_MAPPING = "/anastasia/login";
    private static final String LOGIN_TELEGRAM_MAPPING = "/anastasia/telegram/login";
    private static final String SIGN_UP_MAPPING = "/anastasia/sign-up";
    private static final String SIGN_UP_TELEGRAM_MAPPING = "/anastasia/telegram/sign-up";
    private final static String loginPage = "login";
    private final static String signUpPage = "sign-up";
    private final String telegramBot;
    private final String telegramBotApiUrl;

    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;
    private final RestTemplate restTemplate;


    @Autowired
    public AuthenticationController(PasswordEncoder passwordEncoder, UserDao userDao, RestTemplate restTemplate,
                                    @Value("${telegram_bot.link}") String telegramBot,
                                    @Value("${telegram.rest}") String telegramBotApiUrl) {
        this.telegramBot = telegramBot;
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
        this.restTemplate = restTemplate;
        this.telegramBotApiUrl = telegramBotApiUrl;
    }


    @GetMapping("/sign-up")
    public String signUp(HttpServletRequest request) {
        request.setAttribute(Args.login.toString(), LOGIN_MAPPING);
        request.setAttribute(Args.signUp.toString(), SIGN_UP_MAPPING);
        return signUpPage;
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        request.setAttribute(Args.login.toString(), LOGIN_MAPPING);
        request.setAttribute(Args.signUp.toString(), SIGN_UP_MAPPING);
        return loginPage;
    }

    @PostMapping("/sign-up")
    public String signUpHandle(@RequestParam("email") String login,
                               @RequestParam("password") String password,
                               @RequestParam(name = "name", required = false) String name,
                               HttpServletRequest request) throws ServletException {
        User user = new User(
                login,
                passwordEncoder.encode(password),
                User.Role.USER,
                name != null ? name : "");
        userDao.save(user);
        request.login(login, password);
        return loginPage;
    }

    @GetMapping("/telegram/sign-up")
    public String signUpFromTelegram(@RequestParam("chatId") Long chatId, HttpServletRequest request) throws ServletException {
        Long chatId1;
        if (chatId == null) {
            chatId1 = (Long) request.getSession().getAttribute(Args.chatId.toString());
            if (chatId1 == null) {
                throw new ServletException();
            }
        } else {
            chatId1 = chatId;
        }
        request.getSession().setAttribute(Args.chatId.toString(), chatId1);
        request.setAttribute(Args.login.toString(), LOGIN_TELEGRAM_MAPPING);
        request.setAttribute(Args.signUp.toString(), SIGN_UP_TELEGRAM_MAPPING);
        return signUpPage;
    }

    @GetMapping("/telegram/login")
    public String loginFromTelegram(HttpServletRequest request) throws ServletException {
        Long chatId = (Long) request.getSession().getAttribute(Args.chatId.toString());
        if (chatId == null) {
            throw new ServletException();
        } else {
            request.getSession().setAttribute(Args.chatId.toString(), chatId);
        }
        request.setAttribute(Args.login.toString(), LOGIN_TELEGRAM_MAPPING);
        request.setAttribute(Args.signUp.toString(), SIGN_UP_TELEGRAM_MAPPING);
        return loginPage;
    }

    @PostMapping("/telegram/sign-up")
    public String signUpFromTelegramHandle(@RequestParam("email") String login,
                                           @RequestParam("password") String password,
                                           @RequestParam(name = "name", required = false) String name,
                                           HttpServletRequest request) throws ServletException {
        Long chatId = (Long) request.getAttribute(Args.chatId.toString());
        User user = new User(
                login,
                passwordEncoder.encode(password),
                User.Role.USER,
                name != null ? name : "");
        userDao.save(user);
        if (chatId != null) {
            if (!signUpToTelegram(login, chatId, name)) {
                //TODO
                request.setAttribute("error", "telegram bot signing up failed.");
            }
        }
        request.getSession().removeAttribute(Args.chatId.toString());
        request.login(login, password);
        if (signUpToTelegram(login, chatId, user.getName())) {
            return "redirect:" + telegramBot;
        } else {
            //TODO errors
            return "/";
        }
    }

    @PostMapping("/telegram/login")
    public String loginFromTelegramHandle(@RequestParam("email") String login,
                                          @RequestParam("password") String password,
                                          HttpServletRequest request) throws ServletException {
        User user = userDao.findByLogin(login).orElseThrow();
        Long chatId = (Long) request.getSession().getAttribute(Args.chatId.toString());
        request.getSession().removeAttribute(Args.chatId.toString());
        request.login(login, password);
        if (signUpToTelegram(login, chatId, user.getName())) {
            return "redirect:" + telegramBot;
        } else {
            //TODO errors
            return "/";
        }
    }


    private boolean signUpToTelegram(String login, Long chatId, String name) {
        MultiValueMap<String, Object> parameterMap = new LinkedMultiValueMap<>();
        parameterMap.add(Args.login.toString(), login);
        parameterMap.add(Args.chatId.toString(), chatId);
        parameterMap.add(Args.name.toString(), name);
        HttpEntity<MultiValueMap<String, Object>> parameters = new HttpEntity<>(parameterMap);
        ResponseEntity<Boolean> response = restTemplate
                .exchange(telegramBotApiUrl + "user/register", HttpMethod.POST, parameters, Boolean.class);
        return Boolean.TRUE.equals(response.getBody());
    }

    enum Args {
        login, signUp, name, chatId
    }
}
