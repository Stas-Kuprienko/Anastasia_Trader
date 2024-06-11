package com.stanislav.telegram_bot.domain.handler.message;

public interface MessageHandler {

    String apply(Long chatId);
}
