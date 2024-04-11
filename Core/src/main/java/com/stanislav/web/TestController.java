package com.stanislav.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;

@Controller
public class TestController {

    @PostConstruct
    public void init() {
        System.out.println("i was born");
    }

    @GetMapping("/b")
    public String doing() {
        System.out.println("blablabla");
        return "my_test";
    }
}
