/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.telegram_bot.domain.session;

import com.stanislav.telegram_bot.domain.service.UserDataService;
import com.stanislav.telegram_bot.entities.user.UserChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionContextService {

    //TODO REMAKE BY REDIS CACHE
    private final ConcurrentHashMap<Long, SessionContext> storage;

    private final UserDataService userDataService;

    @Autowired
    public SessionContextService(UserDataService userDataService) {
        this.userDataService = userDataService;
        this.storage = new ConcurrentHashMap<>();
    }


    public SessionContext get(long chatId) {
        SessionContext context = storage.get(chatId);
        if (context == null) {
            context = create(chatId);
        }
        return context;
    }

    public SessionContext create(long chatId) {
        UserChat userChat = userDataService.findByChatId(chatId);
        SessionContext context = new SessionContext();
        context.setChatId(chatId);
        context.setContextState(userChat.getContextState());
        storage.put(chatId, context);
        return context;
    }

    public void save(SessionContext context) {
        storage.put(context.getChatId(), context);
    }
}