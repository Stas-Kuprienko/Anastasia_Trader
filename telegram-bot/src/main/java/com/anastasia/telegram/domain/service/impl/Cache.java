package com.anastasia.telegram.domain.service.impl;

import com.anastasia.telegram.entities.user.User;
import com.anastasia.telegram.entities.user.UserChat;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Cache {

    //TODO TEMPORARY SOLUTION, NEED TO REWORK BY REDIS CACHE

    private final ConcurrentHashMap<Long, UserChat> userChatMapByChatId;
    private final ConcurrentHashMap<Long, UserChat> userChatMapByUserId;

    public Cache() {
        userChatMapByChatId = new ConcurrentHashMap<>();
        userChatMapByUserId = new ConcurrentHashMap<>();
    }

    public Optional<UserChat> get(Long chatId) {
        return Optional.ofNullable(userChatMapByChatId.get(chatId));
    }

    public Optional<UserChat> get(User user) {
        return Optional.ofNullable(userChatMapByUserId.get(user.getId()));
    }

    public void put(UserChat userChat) {
        userChatMapByChatId.put(userChat.getChatId(), userChat);
        userChatMapByUserId.put(userChat.getUser().getId(), userChat);
    }
}
