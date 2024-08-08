/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.telegram.domain.session.impl;

import com.anastasia.telegram.domain.service.UserService;
import com.anastasia.telegram.domain.session.SessionContext;
import com.anastasia.telegram.domain.session.SessionContextService;
import com.anastasia.telegram.entities.user.UserChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionContextServiceImpl implements SessionContextService {

    //TODO REMAKE BY REDIS CACHE
    private final ConcurrentHashMap<Long, SessionContext> storage;

    private final UserService userService;

    @Autowired
    public SessionContextServiceImpl(UserService userService) {
        this.userService = userService;
        this.storage = new ConcurrentHashMap<>();
    }


    @Override
    public SessionContext get(long chatId) {
        SessionContext context = storage.get(chatId);
        if (context == null) {
            context = create(chatId);
        }
        return context;
    }

    @Override
    public SessionContext create(long chatId) {
        UserChat userChat = userService.findByChatId(chatId);
        SessionContext context = new SessionContext();
        context.setChatId(chatId);
        context.setContextState(userChat.getContextState());
        save(context);
        return context;
    }

    @Override
    public void save(SessionContext context) {
        storage.put(context.getChatId(), context);
    }
}