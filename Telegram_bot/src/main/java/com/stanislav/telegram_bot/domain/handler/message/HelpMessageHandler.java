package com.stanislav.telegram_bot.domain.handler.message;

import org.springframework.stereotype.Component;

@Component("/help")
public class HelpMessageHandler implements MessageHandler {

    private static final String manual = """
            Для работы с ботом вы должны быть зарегистрированы на сайте Anastasia-Trade.com.
            
            """;

    @Override
    public String apply(Long chatId) {
        return manual;
    }
}
