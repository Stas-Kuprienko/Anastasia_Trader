/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.trade.entities.user;

import com.anastasia.trade.entities.Broker;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "account")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@RedisHash("account")
public final class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clientId;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user", nullable = false)
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    private Broker broker;

    private String token;

    @OneToOne(mappedBy = "account", cascade = CascadeType.REFRESH)
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