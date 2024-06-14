/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.telegram_bot.domain.user_context;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserContextService {

    private final ConcurrentHashMap<Long, UserContext> contextStorage;

    public UserContextService() {
        this.contextStorage = new ConcurrentHashMap<>();
    }

    //TODO !!!!!

    public UserContext get(long chatId) {
        UserContext context = contextStorage.get(chatId);
        if (context == null) {
            return new UserContext();
        } else {
            return context;
        }
    }

    public UserContext create(long chatId) {
        UserContext context = new UserContext();
        contextStorage.put(chatId, context);
        return context;
    }

    public void save(long chatId, UserContext context) {
        contextStorage.put(chatId, context);
    }
}
