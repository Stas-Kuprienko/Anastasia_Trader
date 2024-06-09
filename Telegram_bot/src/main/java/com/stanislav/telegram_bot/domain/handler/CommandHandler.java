package com.stanislav.telegram_bot.domain.handler;

public interface CommandHandler {

    String handle(Long chatId);
}
