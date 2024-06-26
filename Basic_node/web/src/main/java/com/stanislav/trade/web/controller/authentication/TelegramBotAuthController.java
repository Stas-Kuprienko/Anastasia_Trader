package com.stanislav.trade.web.controller.authentication;

import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.authentication.rest.RestAuthService;
import com.stanislav.trade.web.service.ErrorCase;
import com.stanislav.trade.web.service.ErrorDispatcher;
import com.stanislav.trade.web.service.UserService;
import io.jsonwebtoken.JwtParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/telegram-bot")
public class TelegramBotAuthController {

    private static final String CHAT_ID = "chatId";
    private static final String SIGN_UP_TELEGRAM_MAPPING = "/anastasia/telegram-bot/sign-up";
    private static final String LOGIN_TELEGRAM_MAPPING = "/anastasia/telegram-bot/login";
    private final String telegramBot;
    private final String telegramBotApiUrl;

    private final RestTemplate restTemplate;
    private final JwtParser jwtParser;
    private final ErrorDispatcher errorDispatcher;
    private final UserService userService;
    private final RestAuthService restAuthService;


    @Autowired
    public TelegramBotAuthController(RestTemplate restTemplate, JwtParser jwtParser,
                                     ErrorDispatcher errorDispatcher, UserService userService,
                                     RestAuthService restAuthService,
                                     @Value("${telegram.rest}") String telegramBotApiUrl,
                                     @Value("${telegram_bot.link}") String telegramBot) {
        this.telegramBotApiUrl = telegramBotApiUrl;
        this.telegramBot = telegramBot;
        this.jwtParser = jwtParser;
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.errorDispatcher = errorDispatcher;
        this.restAuthService = restAuthService;
    }


    @GetMapping("/auth")
    public String entry(@RequestParam("chatId") Long chatId,
                        @RequestParam("t") String t, HttpServletRequest request) {
        if (chatId != null && t != null) {
            Long chatIdFromToken = Long.valueOf(
                    jwtParser.parseSignedClaims(t).getPayload().get(CHAT_ID).toString());
            if (chatId.equals(chatIdFromToken)) {
                var principal = request.getUserPrincipal();
                if (principal == null) {
                    request.getSession().setAttribute(CHAT_ID, chatId);
                    return "forward:/telegram-bot/sign-up";
                } else {
                    User user = userService.findByLogin(principal.getName()).orElseThrow();
                    return signUpToTelegram(chatId, user, request);
                }
            }
        }
        String message = errorDispatcher.apply(ErrorCase.TELEGRAM_ID_LOST);
        request.setAttribute("message", message);
        return ErrorDispatcher.ERROR_PAGE;
    }

    @GetMapping("/sign-up")
    public String signUp(HttpServletRequest request) {
        if (request.getSession().getAttribute(CHAT_ID) == null) {
            String message = errorDispatcher.apply(ErrorCase.TELEGRAM_ID_LOST);
            request.setAttribute("message", message);
            return ErrorDispatcher.ERROR_PAGE;
        }
        request.setAttribute(AuthenticationController.Args.login.toString(), LOGIN_TELEGRAM_MAPPING);
        request.setAttribute(AuthenticationController.Args.signUp.toString(), SIGN_UP_TELEGRAM_MAPPING);
        return "sign-up";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        if (request.getSession().getAttribute(CHAT_ID) == null) {
            String message = errorDispatcher.apply(ErrorCase.TELEGRAM_ID_LOST);
            request.setAttribute("message", message);
            return ErrorDispatcher.ERROR_PAGE;
        }
        request.setAttribute(AuthenticationController.Args.login.toString(), LOGIN_TELEGRAM_MAPPING);
        request.setAttribute(AuthenticationController.Args.signUp.toString(), SIGN_UP_TELEGRAM_MAPPING);
        return "login";
    }

    @PostMapping("/sign-up")
    public String signUpHandle(@RequestParam("email") String login,
                                           @RequestParam("password") String password,
                                           @RequestParam(name = "name", required = false) String name,
                                           HttpServletRequest request) {
        Long chatId = (Long) request.getSession().getAttribute(CHAT_ID);
        if (chatId == null) {
            String message = errorDispatcher.apply(ErrorCase.TELEGRAM_ID_LOST);
            request.setAttribute("message", message);
            return ErrorDispatcher.ERROR_PAGE;
        }
        request.getSession().removeAttribute(CHAT_ID);
        User user = userService.create(login, password, name);
        try {
            request.login(login, password);
        } catch (ServletException e) {
            return errorDispatcher.apply(400);
        }
        return signUpToTelegram(chatId, user, request);
    }

    @PostMapping("/login")
    public String loginHandle(@RequestParam("email") String login,
                                          @RequestParam("password") String password,
                                          HttpServletRequest request) {
        Long chatId = (Long) request.getSession().getAttribute(CHAT_ID);
        if (chatId == null) {
            String message = errorDispatcher.apply(ErrorCase.TELEGRAM_ID_LOST);
            request.setAttribute("message", message);
            return ErrorDispatcher.ERROR_PAGE;
        }
        request.getSession().removeAttribute(CHAT_ID);
        try {
            request.login(login, password);
            User user = userService.findByLogin(login).orElseThrow();
            return signUpToTelegram(chatId, user, request);
        } catch (ServletException e) {
            String message = errorDispatcher.apply(400);
            request.setAttribute("message", message);
            return ErrorDispatcher.ERROR_PAGE;
        }
    }


    private String signUpToTelegram(Long chatId, User user, HttpServletRequest request) {
        HttpEntity<MultiValueMap<String, Object>> httpEntity = buildRequestData(chatId, user);
        //TODO remake with json response
        ResponseEntity<Boolean> response = restTemplate
                .exchange(telegramBotApiUrl + "user/register", HttpMethod.POST, httpEntity, Boolean.class);
        if (Boolean.TRUE.equals(response.getBody()) &&
                userService.addTelegramChatId(user, chatId)) {
            return "redirect:" + telegramBot;
        } else {
            String message = errorDispatcher.apply(500);
            request.setAttribute("message", message);
            return ErrorDispatcher.ERROR_PAGE;
        }
    }

    private HttpEntity<MultiValueMap<String, Object>> buildRequestData(Long chatId, User user) {
        MultiValueMap<String, Object> parameterMap = new LinkedMultiValueMap<>();
        parameterMap.add("login", user.getLogin());
        parameterMap.add(CHAT_ID, chatId);
        parameterMap.add("id", user.getId());
        parameterMap.add("name", user.getName());
        //TODO locale
        parameterMap.add("locale", "RU");
        return new HttpEntity<>(parameterMap, restAuthService.authorize());
    }
}
