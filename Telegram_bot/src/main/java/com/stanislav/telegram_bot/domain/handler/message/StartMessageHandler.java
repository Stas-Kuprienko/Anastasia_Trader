package com.stanislav.telegram_bot.domain.handler.message;

public class StartMessageHandler implements MessageHandler {

    private static final String greeting = """
            Привет! Меня зовут Анастасия, я трейд-бот. Могу помочь в биржевой торговле.
                
                Доступные комманды:
                /help - справка по работе с ботом.
                /stocks -
            """;


    @Override
    public String apply(Long chatId) {
        return greeting;
    }
}
