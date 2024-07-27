/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.datasource.jpa;

import com.stanislav.trade.entities.user.TelegramChatId;
import com.stanislav.trade.entities.user.User;

import java.util.Optional;

public interface UserDao extends EntityDao<User, Long> {

    Optional<User> findByLogin(String login);

    boolean addTelegramChatId(User user, Long chatId);

    Optional<TelegramChatId> findTelegramChatId(Long userId);
}
