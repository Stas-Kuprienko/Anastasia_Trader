package com.stanislav.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stanislav.entities.Decimal;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "account")
public final class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String clientId;

    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    @JsonIgnore
    private User user;

    private String broker;

    private String token;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public void setBalance(Decimal balance) {
        this.balance = balance.toBigDecimal();
    }

    public void setBalance(long balance) {
        this.balance = BigDecimal.valueOf(balance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return id == account.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + clientId + '\'' +
                ", user=" + user.getLogin() + '\'' +
                ", broker='" + broker + '\'' +
                ", balance=" + balance+ '\'' +
                ", riskProfile='" + riskProfile +
                '}';
    }
}