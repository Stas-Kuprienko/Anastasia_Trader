package com.stanislav.trade.web.controller.authentication;

import com.stanislav.trade.datasource.UserDao;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.controller.ErrorDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthenticationController {

    private final static String LOGIN_MAPPING = "/anastasia/login";
    private static final String LOGIN_TELEGRAM_MAPPING = "/anastasia/telegram/login";
    private static final String SIGN_UP_MAPPING = "/anastasia/sign-up";
    private static final String SIGN_UP_TELEGRAM_MAPPING = "/anastasia/telegram/sign-up";
    private final static String loginPage = "login";
    private final static String signUpPage = "sign-up";

    private final PasswordEncoder passwordEncoder;
    private final ErrorDispatcher errorDispatcher;
    private final UserDao userDao;
    private final TelegramBotAuthentication telegramBotAuthentication;


    @Autowired
    public AuthenticationController(PasswordEncoder passwordEncoder, ErrorDispatcher errorDispatcher,
                                    UserDao userDao, TelegramBotAuthentication telegramBotAuthentication) {
        this.passwordEncoder = passwordEncoder;
        this.errorDispatcher = errorDispatcher;
        this.userDao = userDao;
        this.telegramBotAuthentication = telegramBotAuthentication;
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
                               HttpServletRequest request) {
        User user = new User(
                login,
                passwordEncoder.encode(password),
                User.Role.USER,
                name != null ? name : "");
        userDao.save(user);
        try {
            request.login(login, password);
            return loginPage;
        } catch (ServletException e) {
            //TODO
            return errorDispatcher.apply(400);
        }
    }

    @GetMapping("/telegram/sign-up")
    public String signUpFromTelegram(@RequestParam("chatId") Long chatId, HttpServletRequest request) {
        Long chatId1;
        if (chatId == null) {
            chatId1 = (Long) request.getSession().getAttribute(Args.chatId.toString());
            if (chatId1 == null) {
                //TODO
                return errorDispatcher.apply(ErrorDispatcher.Case.TELEGRAM_ID_LOST);
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
    public String loginFromTelegram(HttpServletRequest request) {
        Long chatId = (Long) request.getSession().getAttribute(Args.chatId.toString());
        if (chatId == null) {
            return errorDispatcher.apply(ErrorDispatcher.Case.TELEGRAM_ID_LOST);
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
                                           HttpServletRequest request) {
        Long chatId = (Long) request.getSession().getAttribute(Args.chatId.toString());
        if (chatId == null) {
            return errorDispatcher.apply(ErrorDispatcher.Case.TELEGRAM_ID_LOST);
        }
        request.getSession().removeAttribute(Args.chatId.toString());
        User user = new User(
                login,
                passwordEncoder.encode(password),
                User.Role.USER,
                name != null ? name : "");
        userDao.save(user);
        try {
            request.login(login, password);
        } catch (ServletException e) {
            return errorDispatcher.apply(400);
        }
        return telegramBotAuthentication.signUpToTelegram(chatId, user);
    }

    @PostMapping("/telegram/login")
    public String loginFromTelegramHandle(@RequestParam("email") String login,
                                          @RequestParam("password") String password,
                                          HttpServletRequest request) {
        User user = userDao.findByLogin(login).orElseThrow();
        Long chatId = (Long) request.getSession().getAttribute(Args.chatId.toString());
        request.getSession().removeAttribute(Args.chatId.toString());
        try {
            request.login(login, password);
            return telegramBotAuthentication.signUpToTelegram(chatId, user);
        } catch (ServletException e) {
            return errorDispatcher.apply(400);
        }
    }

    @GetMapping("/telegram/confirm")
    public String confirmChatId() {
        return "confirmChatId";
    }

    @PostMapping("/telegram/confirm")
    public String confirmChatIdHandle() {
        //TODO
        return "";
    }


    enum Args {
        login, signUp, name, chatId
    }
}
