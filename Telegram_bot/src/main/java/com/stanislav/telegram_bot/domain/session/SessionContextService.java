/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.telegram_bot.domain.session;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@EnableScheduling
public class SessionContextService {

    private final ConcurrentHashMap<Long, SessionContext> storage;
    private static final long lifeCycle = TimeUnit.DAYS.toMillis(1);

    public SessionContextService() {
        this.storage = new ConcurrentHashMap<>();
    }

    //TODO !!!!!

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.HOURS)
    public void startMonitor() {
        long currentTime = System.currentTimeMillis();
        for (SessionContext s : storage.values()) {
            if ((currentTime - s.getLastActivity()) < SessionContextService.lifeCycle) {
                storage.remove(s.getChatId());
            }
        }
    }

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