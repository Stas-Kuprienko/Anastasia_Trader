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
    private static final String SIGN_UP_MAPPING = "/anastasia/sign-up";

    private final PasswordEncoder passwordEncoder;
    private final ErrorDispatcher errorDispatcher;
    private final UserDao userDao;


    @Autowired
    public AuthenticationController(PasswordEncoder passwordEncoder, ErrorDispatcher errorDispatcher, UserDao userDao) {
        this.passwordEncoder = passwordEncoder;
        this.errorDispatcher = errorDispatcher;
        this.userDao = userDao;
    }


    @GetMapping("/sign-up")
    public String signUp(HttpServletRequest request) {
        request.setAttribute(Args.login.toString(), LOGIN_MAPPING);
        request.setAttribute(Args.signUp.toString(), SIGN_UP_MAPPING);
        return "sign-up";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        request.setAttribute(Args.login.toString(), LOGIN_MAPPING);
        request.setAttribute(Args.signUp.toString(), SIGN_UP_MAPPING);
        return "login";
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
            return "/";
        } catch (ServletException e) {
            //TODO
            return errorDispatcher.apply(400);
        }
    }

    enum Args {
        login, signUp, name
    }
}