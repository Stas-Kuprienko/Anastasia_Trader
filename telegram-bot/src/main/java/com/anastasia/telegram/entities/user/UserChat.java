package com.anastasia.telegram.entities.user;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "user_chat")
//@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserChat {

    @Id
    private Long chatId;

    @Embedded
    private User user;

    @Enumerated(EnumType.STRING)
    private ContextState contextState;


    public UserChat(Long chatId, User user, ContextState contextState) {
        this.chatId = chatId;
        this.user = user;
        this.contextState = contextState;
    }

    public UserChat() {}


    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ContextState getContextState() {
        return contextState;
    }

    public void setContextState(ContextState contextState) {
        this.contextState = contextState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserChat userChat)) return false;
        return Objects.equals(chatId, userChat.chatId) &&
                Objects.equals(user, userChat.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, user);
    }

    @Override
    public String toString() {
        return "UserChat{" +
                "chatId=" + chatId +
                ", user=" + user +
                '}';
    }
}