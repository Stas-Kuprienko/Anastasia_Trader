/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.trade.datasource.jpa;

import com.anastasia.trade.entities.user.TelegramChatId;
import com.anastasia.trade.entities.user.User;

import java.util.Optional;

public interface UserDao extends EntityDao<User, Long> {

    Optional<User> findByLogin(String login);

    boolean addTelegramChatId(User user, Long chatId);

    Optional<TelegramChatId> findTelegramChatId(Long userId);
}
