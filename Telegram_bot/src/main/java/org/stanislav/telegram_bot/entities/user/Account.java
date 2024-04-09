package org.stanislav.telegram_bot.entities.user;

import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Objects;

public final class Account {

    private String clientId;

    private User user;

    private String broker;

    private String token;

    private BigDecimal balance;


    public Account(@NonNull String clientId, @NonNull User user, @NonNull String broker, @NonNull String token) {
        this.clientId = clientId;
        this.user = user;
        this.broker = broker;
        this.token = token;
        this.balance = BigDecimal.valueOf(0);
    }

    public Account() {}


    public String getClientId() {
        return clientId;
    }

    public User getUser() {
        return user;
    }

    public String getBroker() {
        return broker;
    }

    public String getToken() {
        return token;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


    public void setBalance(long balance) {
        this.balance = BigDecimal.valueOf(balance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(clientId, account.clientId) && Objects.equals(user, account.user) && Objects.equals(broker, account.broker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, user, broker);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + clientId + '\'' +
                ", user=" + user.getLogin() +
                ", broker='" + broker + '\'' +
                ", balance=" + balance +
                '}';
    }
}