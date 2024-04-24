package com.stanislav.telegram_bot.entities.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class User {

    private String login;

    private String chatId;

    private String name;

    private List<Account> accounts = new ArrayList<>();


    public User(String login, String chatId, String name) {
        this.login = login;
        this.chatId = chatId;
        this.name = name;
    }

    public User() {}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void setAccount(Account account) {
        this.accounts.add(account);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.login) &&
                Objects.equals(chatId, user.chatId) &&
                Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, chatId, name);
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", chatId='" + chatId + '\'' +
                ", name='" + name + '\'' +
                ", accounts=" + accounts +
                '}';
    }
}