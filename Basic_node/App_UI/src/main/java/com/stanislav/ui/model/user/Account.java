package com.stanislav.ui.model.user;

import com.stanislav.ui.model.Broker;
import lombok.Builder;
import java.util.Objects;

public class Account {

    private Long id;
    private Long userId;
    private String clientId;
    private Broker broker;
    private String token;
    private RiskProfile riskProfile;

    @Builder
    public Account(Long id, Long userId, String clientId, Broker broker, String token, RiskProfile riskProfile) {
        this.id = id;
        this.userId = userId;
        this.clientId = clientId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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
        return Objects.equals(userId, account.userId) &&
                Objects.equals(clientId, account.clientId) &&
                broker == account.broker;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, clientId, broker);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", clientId='" + clientId + '\'' +
                ", broker=" + broker +
                ", riskProfile=" + riskProfile +
                '}';
    }
}
