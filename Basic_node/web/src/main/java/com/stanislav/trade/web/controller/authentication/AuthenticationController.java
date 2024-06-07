package com.stanislav.trade.web.controller.authentication;

import com.stanislav.trade.datasource.UserDao;
import com.stanislav.trade.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthenticationController {

    private final String LOGIN_PAGE = "login";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDao userDao;


    @GetMapping("/sign-up")
    public String signUp() {
        return "sign-up";
    }

    @GetMapping("/login")
    public String login() {
        return LOGIN_PAGE;
    }

    @PostMapping("/sign-up")
    public String signUpHandle(@RequestParam(name = "email") String login,
                               @RequestParam(name = "password") String password,
                               @RequestParam(name = "name", required = false) String name,
                               Model model) {

        User user = new User(login,passwordEncoder.encode(password), User.Role.USER, name != null ? name : "");
        userDao.save(user);
        model.addAttribute("email", login);
        model.addAttribute("password", password);
        return LOGIN_PAGE;
    }

}
