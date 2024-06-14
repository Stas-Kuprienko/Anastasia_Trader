/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.telegram_bot.datasource.cache;

import com.stanislav.telegram_bot.entities.user.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class UserCache {

    //TODO temporary solution
    //TODO will remake by redis

    private final HashMap<Long, User> cache;  //TODO temporary

    public UserCache() {
        this.cache = new HashMap<>();
    }

    public void put(long id, User user) {
        cache.put(id, user);
    }

    public User get(long id) {
        return cache.get(id);
    }

    public Optional<User> get(String login) {
        return cache.values()
                .stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst();
    }
}
