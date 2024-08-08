package com.anastasia.ui.controller.authentication;

import com.anastasia.ui.configuration.auth.TokenAuthService;
import com.anastasia.ui.controller.service.ErrorCase;
import com.anastasia.ui.controller.service.ErrorController;
import com.anastasia.ui.controller.service.MVC;
import com.anastasia.ui.model.user.User;
import com.anastasia.ui.service.UserService;
import io.jsonwebtoken.JwtParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
    private final UserService userService;
    private final TokenAuthService tokenAuthService;


    @Autowired
    public TelegramBotAuthController(RestTemplate restTemplate, JwtParser jwtParser,
                                     UserService userService, TokenAuthService tokenAuthService,
                                     @Value("${telegram.rest}") String telegramBotApiUrl,
                                     @Value("${telegram_bot.link}") String telegramBot) {
        this.telegramBotApiUrl = telegramBotApiUrl;
        this.telegramBot = telegramBot;
        this.jwtParser = jwtParser;
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.tokenAuthService = tokenAuthService;
    }


    @GetMapping("/auth")
    public String entry(@RequestParam(value = "chatId", required = false) Long chatId,
                        @RequestParam(value = "t", required = false) String t,
                        HttpServletRequest request) {
        if (chatId != null && t != null) {
            Long chatIdFromToken = Long.valueOf(
                    jwtParser.parseSignedClaims(t).getPayload().get(CHAT_ID).toString());
            if (chatId.equals(chatIdFromToken)) {
                var principal = request.getUserPrincipal();
                if (principal == null) {
                    request.getSession().setAttribute(CHAT_ID, chatId);
                    return MVC.FORWARD + "/telegram-bot/sign-up";
                } else {
                    User user = userService.findByLogin(principal.getName());
                    return signUpToTelegram(chatId, user);
                }
            }
        }
        return ErrorController.FORWARD_ERROR + ErrorCase.TELEGRAM_ID_LOST;
    }

    @GetMapping("/sign-up")
    public String signUp(HttpServletRequest request) {
        if (request.getSession().getAttribute(CHAT_ID) == null) {
            return ErrorController.FORWARD_ERROR + ErrorCase.TELEGRAM_ID_LOST;
        }
        request.setAttribute(AuthenticationController.LOGIN_ATTRIBUTE, LOGIN_TELEGRAM_MAPPING);
        request.setAttribute(AuthenticationController.SIGN_UP_ATTRIBUTE, SIGN_UP_TELEGRAM_MAPPING);
        return "sign-up";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        if (request.getSession().getAttribute(CHAT_ID) == null) {
            return ErrorController.FORWARD_ERROR + ErrorCase.TELEGRAM_ID_LOST;
        }
        request.setAttribute(AuthenticationController.Args.login.toString(), LOGIN_TELEGRAM_MAPPING);
        request.setAttribute(AuthenticationController.Args.signUp.toString(), SIGN_UP_TELEGRAM_MAPPING);
        return MVC.LOGIN_PAGE;
    }

    @PostMapping("/sign-up")
    public String signUpHandle(@RequestParam("email") String login,
                               @RequestParam("password") String password,
                               @RequestParam(name = "name", required = false) String name,
                               HttpServletRequest request) {
        Long chatId = (Long) request.getSession().getAttribute(CHAT_ID);
        if (chatId == null) {
            return ErrorController.REDIRECT_ERROR + ErrorCase.TELEGRAM_ID_LOST;
        }
        User user = userService.registry(login, password, name);
        try {
            request.login(login, password);
            request.getSession().removeAttribute(CHAT_ID);
        } catch (ServletException e) {
            log.error(e.getMessage());
            return MVC.REDIRECT + "/sign-up";
        }
        return signUpToTelegram(chatId, user);
    }

    @PostMapping("/login")
    public String loginHandle(@RequestParam("email") String login,
                              @RequestParam("password") String password,
                              HttpServletRequest request) {
        Long chatId = (Long) request.getSession().getAttribute(CHAT_ID);
        if (chatId == null) {
            return ErrorController.REDIRECT_ERROR + ErrorCase.TELEGRAM_ID_LOST;
        }
        try {
            request.login(login, password);
            request.getSession().removeAttribute(CHAT_ID);
            User user = userService.findByLogin(login);
            return signUpToTelegram(chatId, user);
        } catch (ServletException e) {
            log.warn(e.getMessage());
            return MVC.REDIRECT + "/login?error=true";
        }
    }


    private String signUpToTelegram(Long chatId, User user) {
        HttpEntity<MultiValueMap<String, Object>> httpEntity = buildRequestData(chatId, user);
        //TODO remake with json response
        ResponseEntity<Boolean> response = restTemplate
                .exchange(telegramBotApiUrl + "user/register", HttpMethod.POST, httpEntity, Boolean.class);
        if (Boolean.TRUE.equals(response.getBody())) {
            return MVC.REDIRECT + telegramBot;
        } else {
            return ErrorController.REDIRECT_ERROR + ErrorCase.DEFAULT;
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
        return new HttpEntity<>(parameterMap, tokenAuthService.authorize());
    }
}
