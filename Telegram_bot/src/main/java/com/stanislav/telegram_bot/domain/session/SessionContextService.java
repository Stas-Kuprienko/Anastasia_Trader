/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.telegram_bot.domain.session;

public interface SessionContextService {

    SessionContext get(long chatId);

    SessionContext create(long chatId);

    void save(SessionContext context);
}
