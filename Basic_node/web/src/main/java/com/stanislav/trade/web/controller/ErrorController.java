package com.stanislav.trade.web.controller;

import com.stanislav.trade.web.service.ErrorCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Locale;

@Slf4j
@Controller
public class ErrorController {

    private final MessageSource messageSource;

    @Autowired
    public ErrorController(@Qualifier("errorMessageSource") MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/error/{case}")
    public String errorHandle(@PathVariable("case") String errorCase,
                              @RequestParam(value = "lang", required = false) String lang,
                              HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException {
        ErrorCase ec = ErrorCase.valueOf(errorCase);
        Locale locale;
        try {
            //TODO locale
            if (lang == null) {
                lang = "RU";
            }
            locale = Locale.of(lang);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(e);
        }
        ErrorModel em = switch (ec) {
            case ACCESS_DENIED -> new ErrorModel(
                    messageSource.getMessage(errorCase, null, locale),
                    Images.ACCESS_DENIED.file, 403);
            case TELEGRAM_ID_LOST -> new ErrorModel(
                    messageSource.getMessage(errorCase, null, locale),
                    Images.DEFAULT.file, 400);
            default -> new ErrorModel("", Images.DEFAULT.file, 500);
        };
        request.setAttribute("error", em);
        response.setStatus(em.code);
        return "error_page";
    }


    public enum Images {

        ACCESS_DENIED("access-denied.webp"),
        DEFAULT("default-error.webp");

        public final String file;

        Images(String file) {
            this.file = file;
        }
    }

    public record ErrorModel(String message, String image, int code) {}
}