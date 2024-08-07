package com.anastasia.ui.controller.authentication;

import com.anastasia.ui.configuration.auth.form.MyUserDetails;
import com.anastasia.ui.controller.service.MVC;
import com.anastasia.ui.model.user.User;
import com.anastasia.ui.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class AuthenticationController {

    public static final String LOGIN_ATTRIBUTE = "login";
    public static final String SIGN_UP_ATTRIBUTE = "signUp";
    private final static String LOGIN_MAPPING = "/anastasia/login";
    private static final String SIGN_UP_MAPPING = "/anastasia/sign-up";

    private final UserService userService;


    @Autowired
    public AuthenticationController(UserService userService) {
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
        request.setAttribute(LOGIN_ATTRIBUTE, LOGIN_MAPPING);
        request.setAttribute(SIGN_UP_ATTRIBUTE, SIGN_UP_MAPPING);
        return MVC.LOGIN_PAGE;
    }

    @PostMapping("/sign-up")
    public String signUpHandle(@RequestParam("email") String login,
                               @RequestParam("password") String password,
                               @RequestParam(name = "name", required = false) String name,
                               HttpServletRequest request) {
        User user = userService.registry(login, password, name);
        try {
            request.login(login, password);
            request.getSession().setAttribute("id", user.getId());
            return MVC.REDIRECT + "/user";
        } catch (ServletException e) {
            log.error(e.getMessage());
            return MVC.REDIRECT + "/sign-up";
        }
    }

    @PostMapping("/login/auth")
    public String loginHandle(@AuthenticationPrincipal UserDetails userDetails) {
        User.Role role = ((MyUserDetails) userDetails).getRole();
        return switch (role) {
            case USER -> MVC.REDIRECT + "/user";
            case ADMIN -> MVC.REDIRECT + "/admin";
            case null, default -> {
                log.error("user role is " + role);
                yield MVC.REDIRECT + "/login?error=true";
            }
        };
    }

    @GetMapping("/user")
    public String mainPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return MVC.FORWARD + "/login";
        }
        User user = userService.findById(((MyUserDetails) userDetails).getId());
        model.addAttribute("user", user);
        return "main";
    }


    enum Args {
        login, signUp, name
    }
}