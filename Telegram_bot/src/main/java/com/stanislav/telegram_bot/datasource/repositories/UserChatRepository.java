package com.stanislav.telegram_bot.datasource.repositories;

import com.stanislav.telegram_bot.entities.user.User;
import com.stanislav.telegram_bot.entities.user.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserChatRepository extends JpaRepository<UserChat, Long> {

    Optional<UserChat> findById(long userId);
}
