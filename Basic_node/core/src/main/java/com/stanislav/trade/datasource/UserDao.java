/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.datasource;

import com.stanislav.trade.entities.user.User;

import java.util.Optional;

public interface UserDao extends EntityDao<User, Long> {

    Optional<User> findByLogin(String login);
}
