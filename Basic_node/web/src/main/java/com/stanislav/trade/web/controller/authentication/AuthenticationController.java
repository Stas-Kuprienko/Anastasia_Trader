package com.stanislav.trade.web.controller.authentication;

import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.service.UserDataService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;

@Slf4j
@Controller
public class AuthenticationController {

    public static final String LOGIN_ATTRIBUTE = "login";
    public static final String SIGN_UP_ATTRIBUTE = "signUp";
    private final static String LOGIN_MAPPING = "/anastasia/login";
    private static final String SIGN_UP_MAPPING = "/anastasia/sign-up";

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
        request.setAttribute(LOGIN_ATTRIBUTE, LOGIN_MAPPING);
        request.setAttribute(SIGN_UP_ATTRIBUTE, SIGN_UP_MAPPING);
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
            return "redirect:/user";
        } catch (ServletException e) {
            log.error(e.getMessage());
            return "redirect:/sign-up";
        }
    }

    @PostMapping("/login/auth")
    public String loginHandle(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
        Optional<User> user = userDataService.findByLogin(userDetails.getUsername());
        if (user.isPresent()) {
            request.getSession().setAttribute("id", user.get().getId());
            if (user.get().getRole().equals(User.Role.USER)) {
                return "redirect:/user";
            } else if (user.get().getRole().equals(User.Role.ADMIN)) {
                return "redirect:/admin";
            } else {
                log.error("user role is " + user.get().getRole());
            }
        }
        return "redirect:/login?error=true";
    }

    @GetMapping("/user")
    public String mainPage(HttpSession session, Model model) {
        Long id = (Long) session.getAttribute("id");
        if (id == null) {
            log.error("User ID is lost");
            return "redirect:/login";
        }
        User user = userDataService.findById(id).orElseThrow();
        model.addAttribute("user", user);
        return "main";
    }


    enum Args {
        login, signUp, name
    }
}