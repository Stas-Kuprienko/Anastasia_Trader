package com.stanislav.trade.web.controller.authentication;

import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.service.UserDataService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Controller
public class AuthenticationController {

    private final static String LOGIN_MAPPING = "/anastasia/login";
    private static final String SIGN_UP_MAPPING = "/anastasia/sign-up";
    private static final String LOGIN_ERROR = "login_error";

    private final UserDataService userDataService;


    @Autowired
    public AuthenticationController(UserDataService userDataService) {
        this.userDataService = userDataService;
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
        User user = userDataService.create(login, password, name);
        try {
            request.login(login, password);
            request.getSession().setAttribute("id", user.getId());
            return "redirect:/user/" + user.getId();
        } catch (ServletException e) {
            log.error(e.getMessage());
            //TODO
            return "redirect:/sign-up";
        }
    }

    @PostMapping("/login/auth")
    public String loginHandle(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
        Optional<User> user = userDataService.findByLogin(userDetails.getUsername());
        if (user.isPresent()) {
            request.getSession().setAttribute("id", user.get().getId());
            if (user.get().getRole().equals(User.Role.USER)) {
                return "redirect:/user/" + user.get().getId();
            } else if (user.get().getRole().equals(User.Role.ADMIN)) {
                return "redirect:/admin/" + user.get().getId();
            } else {
                log.error("user role is " + user.get().getRole());
            }
        }
        return "redirect:/login?error=true";
    }

    @GetMapping("/user/{id}")
    public String mainPage(@PathVariable("id") String id, Model model) {
        Long userId = Long.valueOf(id);
        User user = userDataService.findById(userId).orElseThrow();
        model.addAttribute("user", user);
        return "main";
    }


    enum Args {
        login, signUp, name
    }
}