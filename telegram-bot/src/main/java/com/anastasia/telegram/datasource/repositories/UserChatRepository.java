package com.anastasia.telegram.datasource.repositories;

import com.anastasia.telegram.entities.user.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserChatRepository extends JpaRepository<UserChat, Long> {

    Optional<UserChat> findById(long userId);
}
