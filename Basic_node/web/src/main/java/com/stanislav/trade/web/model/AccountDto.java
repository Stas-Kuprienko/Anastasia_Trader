package com.stanislav.trade.web.model;

import com.stanislav.trade.entities.user.Account;
import java.math.BigDecimal;

public final class AccountDto {

    private final String clientId;

    private final String broker;

    private final String token;

    private final BigDecimal balance;


    public AccountDto(Account account) {
        this.clientId = account.getClientId();
        this.broker = account.getBroker();
        this.token = account.getToken();
        this.balance = account.getBalance();
    }


    public String getClientId() {
        return clientId;
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
}