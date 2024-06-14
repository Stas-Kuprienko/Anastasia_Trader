/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.telegram_bot.domain.session;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionContextService {

    private final ConcurrentHashMap<Long, SessionContext> storage;

    public SessionContextService() {
        this.storage = new ConcurrentHashMap<>();
    }

    //TODO !!!!!

    public SessionContext get(long chatId) {
        SessionContext context = storage.get(chatId);
        if (context == null) {
            context = create(chatId);
        }
        return context;
    }

    public SessionContext create(long chatId) {
        SessionContext context = new SessionContext();
        context.setChatId(chatId);
        storage.put(chatId, context);
        return context;
    }

    public void save(SessionContext context) {
        storage.put(context.getChatId(), context);
    }
}