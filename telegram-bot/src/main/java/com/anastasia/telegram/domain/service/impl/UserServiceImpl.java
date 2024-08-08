package com.anastasia.telegram.domain.service.impl;

import com.anastasia.telegram.datasource.repositories.UserChatRepository;
import com.anastasia.telegram.domain.service.UserService;
import com.anastasia.telegram.entities.user.User;
import com.anastasia.telegram.entities.user.UserChat;
import com.anastasia.telegram.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("userService")
public class UserServiceImpl implements UserService {

    //TODO TEMPORARY SOLUTION, NEED TO REWORK BY REDIS CACHE
    private final Cache cache;

    private final UserChatRepository userChatRepository;

    @Autowired
    public UserServiceImpl(Cache cache, UserChatRepository userChatRepository) {
        this.cache = cache;
        this.userChatRepository = userChatRepository;
    }


    @Override
    public boolean isRegistered(long chatId) {
        Optional<UserChat> userChat = cache.get(chatId);
        if (userChat.isEmpty()) {
            userChat = userChatRepository.findById(chatId);
        }
        return userChat.isPresent();
    }

    @Override
    public UserChat findByChatId(Long chatId) {
        Optional<UserChat> userChat = cache.get(chatId);
        if (userChat.isEmpty()) {
            userChat = userChatRepository.findById(chatId);
            if (userChat.isEmpty()) {
                throw new NotFoundException("userChat " + chatId + " is not found");
            }
        }
        return userChat.get();
    }

    @Override
    public UserChat findByUser(User user) {
        Optional<UserChat> userChat = cache.get(user);
        if (userChat.isEmpty()) {
            userChat = userChatRepository.findById(user.getId());
            if (userChat.isEmpty()) {
                throw new NotFoundException("userChat " + user + " is not found");
            }
        }
        return userChat.get();
    }

    @Override
    public void save(UserChat userChat) {
        userChatRepository.save(userChat);
        cache.put(userChat);
    }
}