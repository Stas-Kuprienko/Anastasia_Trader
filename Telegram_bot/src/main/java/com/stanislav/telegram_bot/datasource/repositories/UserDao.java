package com.stanislav.telegram_bot.datasource.repositories;

import com.stanislav.telegram_bot.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);
}