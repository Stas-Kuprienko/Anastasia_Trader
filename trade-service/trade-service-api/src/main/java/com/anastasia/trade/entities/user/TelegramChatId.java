package com.anastasia.trade.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "telegram_chat_id")
public class TelegramChatId {

    @Id
    private Long chatId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user", nullable = false)
    @JsonIgnore
    private User user;


    public TelegramChatId(Long chatId, User user) {
        this.chatId = chatId;
        this.user = user;
    }

    public TelegramChatId() {}


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TelegramChatId that)) return false;
        return Objects.equals(chatId, that.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }

    @Override
    public String toString() {
        return "TelegramChatId{" +
                "chatId=" + chatId +
                ", user=" + user +
                '}';
    }
}