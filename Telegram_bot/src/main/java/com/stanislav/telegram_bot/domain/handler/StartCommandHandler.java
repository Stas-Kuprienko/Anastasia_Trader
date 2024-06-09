package com.stanislav.telegram_bot.domain.handler;

public class StartCommandHandler implements CommandHandler {

    private static final String greeting = """
            Привет! Меня зовут Анастасия, я трейд-бот. Могу помочь в биржевой торговле.
                
                Доступные комманды:
                /help - справка по работе с ботом.
                /stocks -
            """;


    @Override
    public String handle(Long chatId) {
        return greeting;
    }
}
