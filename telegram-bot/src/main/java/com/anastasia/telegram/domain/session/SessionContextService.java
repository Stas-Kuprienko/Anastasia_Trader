/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.telegram.domain.session;

public interface SessionContextService {

    SessionContext get(long chatId);

    SessionContext create(long chatId);

    void save(SessionContext context);
}
