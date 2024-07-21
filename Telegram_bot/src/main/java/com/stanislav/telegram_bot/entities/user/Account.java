package com.stanislav.telegram_bot.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stanislav.telegram_bot.entities.Broker;
import java.io.Serializable;
import java.util.Objects;

public final class Account implements Serializable {

    private Long id;

    private String clientId;

    @JsonIgnore
    private User user;

    private Broker broker;

    private String token;

    private RiskProfile riskProfile;

    public Account(long id, String clientId, User user, Broker broker, String token, RiskProfile riskProfile) {
        this.id = id;
        this.clientId = clientId;
        this.user = user;
        this.broker = broker;
        this.token = token;
        this.riskProfile = riskProfile;
    }

    public Account() {}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public RiskProfile getRiskProfile() {
        return riskProfile;
    }

    public void setRiskProfile(RiskProfile riskProfile) {
        this.riskProfile = riskProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return Objects.equals(clientId, account.clientId) &&
                Objects.equals(user, account.user) &&
                Objects.equals(broker, account.broker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, user, broker);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", clientId='" + clientId + '\'' +
                ", userId=" + user.getId() +
                ", broker='" + broker + '\'' +
                '}';
    }
}