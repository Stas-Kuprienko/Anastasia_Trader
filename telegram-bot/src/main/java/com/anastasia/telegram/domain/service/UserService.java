package com.anastasia.telegram.domain.service;

import com.anastasia.telegram.entities.user.User;
import com.anastasia.telegram.entities.user.UserChat;

public interface UserService {

    boolean isRegistered(long chatId);

    UserChat findByChatId(Long chatId);

    UserChat findByUser(User user);

    void save(UserChat userChat);
}
