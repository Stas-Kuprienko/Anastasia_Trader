/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "account")
public final class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(nullable = false)
    private String clientId;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user", nullable = false)
    @JsonIgnore
    private User user;

    private String broker;

    private String token;

    @OneToOne(mappedBy = "account", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JsonIgnore
    private RiskProfile riskProfile;

    private BigDecimal balance;


    public Account(long id, String clientId, User user, String broker, String token, RiskProfile riskProfile, BigDecimal balance) {
        this.id = id;
        this.clientId = clientId;
        this.user = user;
        this.broker = broker;
        this.token = token;
        this.riskProfile = riskProfile;
        this.balance = balance;
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

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", clientId='" + clientId + '\'' +
                ", userId=" + user.getId() +
                ", broker='" + broker + '\'' +
                ", balance=" + balance +
                '}';
    }
}