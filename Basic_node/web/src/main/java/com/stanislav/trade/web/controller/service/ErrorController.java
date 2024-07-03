package com.stanislav.trade.web.controller.service;

import com.stanislav.trade.web.service.ErrorCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Locale;

@Slf4j
@Controller
public class ErrorController {

    public static final String URL = "forward:/error/";
    public static final String ERROR_PAGE = "error_page";
    public static final String ERROR_KEY = "error";

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

            case NOT_FOUND -> new ErrorModel(
                    messageSource.getMessage(errorCase, null, locale),
                    Images.NOT_FOUND.file, 404);

            case TELEGRAM_ID_LOST -> new ErrorModel(
                    messageSource.getMessage(errorCase, null, locale),
                    Images.DEFAULT.file, 400);

            case BAD_REQUEST -> new ErrorModel(
                    messageSource.getMessage(errorCase, null, locale),
                    Images.ACCESS_DENIED.file, 400);

            default -> new ErrorModel(messageSource.getMessage("500", null, locale),
                    Images.SERVER_ERROR.file, 500);
        };
        request.setAttribute(ERROR_KEY, em);
        response.setStatus(em.code);
        return ERROR_PAGE;
    }

    @GetMapping("/error/404")
    public String error404(Model model) {
        ErrorModel errorModel = new ErrorModel(
                messageSource.getMessage(ErrorCase.NOT_FOUND.toString(),
                        null,
                        Locale.of("RU")), Images.NOT_FOUND.file, 404);
        model.addAttribute(ERROR_KEY, errorModel);
        return ERROR_PAGE;
    }

    @GetMapping("/error/500")
    public String error500(Model model) {
        ErrorModel errorModel = new ErrorModel(
                messageSource.getMessage("500",
                        null,
                        Locale.of("RU")), Images.SERVER_ERROR.file, 500);
        model.addAttribute(ERROR_KEY, errorModel);
        return ERROR_PAGE;
    }


    public enum Images {

        ACCESS_DENIED("access-denied.webp"),
        NOT_FOUND("not-found.webp"),
        SERVER_ERROR("server_error.webp"),
        DEFAULT("default-error.webp");

        public final String file;

        Images(String file) {
            this.file = file;
        }
    }

    public record ErrorModel(String message, String image, int code) {}
}