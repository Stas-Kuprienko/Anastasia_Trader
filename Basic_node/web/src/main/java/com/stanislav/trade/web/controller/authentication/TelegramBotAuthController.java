package com.stanislav.trade.web.controller.authentication;

import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.authentication.rest.RestAuthService;
import com.stanislav.trade.web.service.ErrorCase;
import com.stanislav.trade.web.service.UserDataService;
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
    private final UserDataService userDataService;
    private final RestAuthService restAuthService;


    @Autowired
    public TelegramBotAuthController(RestTemplate restTemplate, JwtParser jwtParser,
                                     UserDataService userDataService, RestAuthService restAuthService,
                                     @Value("${telegram.rest}") String telegramBotApiUrl,
                                     @Value("${telegram_bot.link}") String telegramBot) {
        this.telegramBotApiUrl = telegramBotApiUrl;
        this.telegramBot = telegramBot;
        this.jwtParser = jwtParser;
        this.restTemplate = restTemplate;
        this.userDataService = userDataService;
        this.restAuthService = restAuthService;
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
                    return "forward:/telegram-bot/sign-up";
                } else {
                    User user = userDataService.findByLogin(principal.getName()).orElseThrow();
                    return signUpToTelegram(chatId, user);
                }
            }
        }
        return "forward:/error/" + ErrorCase.TELEGRAM_ID_LOST;
    }

    @GetMapping("/sign-up")
    public String signUp(HttpServletRequest request) {
        if (request.getSession().getAttribute(CHAT_ID) == null) {
            return "forward:/error/" + ErrorCase.TELEGRAM_ID_LOST;
        }
        request.setAttribute(AuthenticationController.LOGIN_ATTRIBUTE, LOGIN_TELEGRAM_MAPPING);
        request.setAttribute(AuthenticationController.SIGN_UP_ATTRIBUTE, SIGN_UP_TELEGRAM_MAPPING);
        return "sign-up";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        if (request.getSession().getAttribute(CHAT_ID) == null) {
            return "forward:/error/" + ErrorCase.TELEGRAM_ID_LOST;
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
            return "forward:/error/" + ErrorCase.TELEGRAM_ID_LOST;
        }
        User user = userDataService.create(login, password, name);
        try {
            request.login(login, password);
            request.getSession().removeAttribute(CHAT_ID);
        } catch (ServletException e) {
            log.error(e.getMessage());
            return "redirect:/sign-up";
        }
        return signUpToTelegram(chatId, user);
    }

    @PostMapping("/login")
    public String loginHandle(@RequestParam("email") String login,
                              @RequestParam("password") String password,
                              HttpServletRequest request) {
        Long chatId = (Long) request.getSession().getAttribute(CHAT_ID);
        if (chatId == null) {
            return "forward:/error/" + ErrorCase.TELEGRAM_ID_LOST;
        }
        try {
            request.login(login, password);
            request.getSession().removeAttribute(CHAT_ID);
            User user = userDataService.findByLogin(login).orElseThrow();
            return signUpToTelegram(chatId, user);
        } catch (ServletException e) {
            log.warn(e.getMessage());
            return "redirect:/login?error=true";
        }
    }


    private String signUpToTelegram(Long chatId, User user) {
        HttpEntity<MultiValueMap<String, Object>> httpEntity = buildRequestData(chatId, user);
        //TODO remake with json response
        ResponseEntity<Boolean> response = restTemplate
                .exchange(telegramBotApiUrl + "user/register", HttpMethod.POST, httpEntity, Boolean.class);
        if (Boolean.TRUE.equals(response.getBody()) &&
                userDataService.addTelegramChatId(user, chatId)) {
            return "redirect:" + telegramBot;
        } else {
            return "forward:/error/" + ErrorCase.DEFAULT;
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
