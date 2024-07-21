package com.stanislav.telegram_bot.domain.service;

import com.stanislav.telegram_bot.entities.user.User;
import com.stanislav.telegram_bot.entities.user.UserChat;

public interface UserDataService {
    boolean isRegistered(long chatId);

    UserChat findByChatId(Long chatId);

    UserChat findByUser(User user);

    void save(UserChat userChat);
}
