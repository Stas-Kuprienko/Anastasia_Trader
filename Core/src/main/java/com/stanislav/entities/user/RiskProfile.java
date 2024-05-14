package com.stanislav.entities.user;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "risk_profile")
public class RiskProfile {

    @Id
    private long accountId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "account")
    private Account account;

    private byte dealLossPercentage;

    private byte accountLossPercentage;

    private byte futuresInAccountPercentage;

    private byte StockInAccountPercentage;

    @Enumerated(EnumType.STRING)
    private RiskType riskType;


    public RiskProfile(long accountId, Account account, byte dealLossPercentage, byte accountLossPercentage,
                       byte futuresInAccountPercentage, byte stockInAccountPercentage, RiskType riskType) {
        this.accountId = accountId;
        this.account = account;
        this.dealLossPercentage = dealLossPercentage;
        this.accountLossPercentage = accountLossPercentage;
        this.futuresInAccountPercentage = futuresInAccountPercentage;
        StockInAccountPercentage = stockInAccountPercentage;
        this.riskType = riskType;
    }

    public RiskProfile() {}


    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public byte getDealLossPercentage() {
        return dealLossPercentage;
    }

    public void setDealLossPercentage(byte dealLossPercentage) {
        this.dealLossPercentage = dealLossPercentage;
    }

    public byte getAccountLossPercentage() {
        return accountLossPercentage;
    }

    public void setAccountLossPercentage(byte accountLossPercentage) {
        this.accountLossPercentage = accountLossPercentage;
    }

    public byte getFuturesInAccountPercentage() {
        return futuresInAccountPercentage;
    }

    public void setFuturesInAccountPercentage(byte futuresInAccountPercentage) {
        this.futuresInAccountPercentage = futuresInAccountPercentage;
    }

    public byte getStockInAccountPercentage() {
        return StockInAccountPercentage;
    }

    public void setStockInAccountPercentage(byte stockInAccountPercentage) {
        StockInAccountPercentage = stockInAccountPercentage;
    }

    public RiskType getRiskType() {
        return riskType;
    }

    public void setRiskType(RiskType riskType) {
        this.riskType = riskType;
    }


    public enum RiskType {

        conservative,
        medium,
        aggressive

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RiskProfile that)) return false;
        return accountId == that.accountId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(accountId);
    }

    @Override
    public String toString() {
        return "RiskProfile{" +
                "accountId=" + accountId +
                ", dealLossPercentage=" + dealLossPercentage +
                ", accountLossPercentage=" + accountLossPercentage +
                ", futuresInAccountPercentage=" + futuresInAccountPercentage +
                ", StockInAccountPercentage=" + StockInAccountPercentage +
                ", riskType=" + riskType +
                '}';
    }
}
