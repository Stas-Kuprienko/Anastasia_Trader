package com.stanislav.trade.web.controller;

import com.stanislav.trade.domain.smart.SmartAutoTradeService;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.service.UserDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/smart")
public class SmartTradeController {

    private final UserDataService userDataService;
    private final SmartAutoTradeService smartAutoTradeService;

    @Autowired
    public SmartTradeController(UserDataService userDataService, SmartAutoTradeService smartAutoTradeService) {
        this.userDataService = userDataService;
        this.smartAutoTradeService = smartAutoTradeService;
    }

    @GetMapping("/select")
    public String selectSmart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Optional<User> user = userDataService.findUserByLogin(userDetails.getUsername());
        if (user.isEmpty()) {
            log.error("user=" + userDetails.getUsername() + " is lost");
            return "login";
        }
        Set<String> strategies = smartAutoTradeService.getStrategies();
        model.addAttribute("accounts", user.get().getAccounts());
        model.addAttribute("strategies", strategies);
        return "select_smart";
    }


}
