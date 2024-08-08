/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.telegram.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class RiskProfile {

    private Long accountId;

    @JsonIgnore
    private Account account;

    private byte dealLossPercentage;

    private byte accountLossPercentage;

    private byte futuresInAccountPercentage;

    private byte StockInAccountPercentage;

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
        return Objects.equals(accountId, that.accountId) &&
                Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, account);
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
