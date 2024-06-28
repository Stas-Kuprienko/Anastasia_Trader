package com.stanislav.trade.web.controller.authentication;

import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.service.ErrorDispatcher;
import com.stanislav.trade.web.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthenticationController {

    private final static String LOGIN_MAPPING = "/anastasia/login";
    private static final String SIGN_UP_MAPPING = "/anastasia/sign-up";

    private final ErrorDispatcher errorDispatcher;
    private final UserService userService;


    @Autowired
    public AuthenticationController(ErrorDispatcher errorDispatcher, UserService userService) {
        this.errorDispatcher = errorDispatcher;
        this.userService = userService;
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
        User user = userService.create(login, password, name);
        try {
            request.login(login, password);
            request.getSession().setAttribute("id", user.getId());
            return "redirect:/user/" + user.getId();
        } catch (ServletException e) {
            //TODO
            return errorDispatcher.apply(400);
        }
    }

    @PostMapping("/login/auth")
    public String loginHandle(@AuthenticationPrincipal UserDetails userDetails, HttpSession session) {
        Optional<User> user = userService.findByLogin(userDetails.getUsername());
        if (user.isPresent()) {
            session.setAttribute("id", user.get().getId());
            if (user.get().getRole().equals(User.Role.USER)) {
                return "redirect:/user/" + user.get().getId();
            } else if (user.get().getRole().equals(User.Role.ADMIN)) {
                return "redirect:/admin/" + user.get().getId();
            } else {
                //TODO errors
                return errorDispatcher.apply(0);
            }
        }
        //TODO errors
        return errorDispatcher.apply(0);
    }

    @GetMapping("/user/{id}")
    public String mainPage(@PathVariable("id") String id, Model model) {
        Long userId = Long.valueOf(id);
        User user = userService.findById(userId).orElseThrow();
        model.addAttribute("user", user);
        return "main";
    }


    enum Args {
        login, signUp, name
    }
}