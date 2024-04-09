package org.stanislav.telegram_bot.entities.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class User {

    private String login;

    private String name;

    private List<Account> accounts = new ArrayList<>();


    public User(String login, String name) {
        this.login = login;
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
        return Objects.equals(name, user.name) && Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, login);
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", accounts=" + accounts +
                '}';
    }
}