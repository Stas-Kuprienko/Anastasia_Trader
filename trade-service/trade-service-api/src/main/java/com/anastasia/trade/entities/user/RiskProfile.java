/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.trade.entities.user;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class RiskProfile {

    private byte dealLossPercentage;

    private byte accountLossPercentage;

    private byte futuresInAccountPercentage;

    private byte StockInAccountPercentage;

    private RiskType riskType;


    public RiskProfile(byte dealLossPercentage, byte accountLossPercentage,
                       byte futuresInAccountPercentage, byte stockInAccountPercentage, RiskType riskType) {
        this.dealLossPercentage = dealLossPercentage;
        this.accountLossPercentage = accountLossPercentage;
        this.futuresInAccountPercentage = futuresInAccountPercentage;
        StockInAccountPercentage = stockInAccountPercentage;
        this.riskType = riskType;
    }

    public RiskProfile() {}


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
        return dealLossPercentage == that.dealLossPercentage &&
                accountLossPercentage == that.accountLossPercentage &&
                futuresInAccountPercentage == that.futuresInAccountPercentage &&
                StockInAccountPercentage == that.StockInAccountPercentage &&
                riskType == that.riskType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dealLossPercentage,
                accountLossPercentage,
                futuresInAccountPercentage,
                StockInAccountPercentage, riskType);
    }

    @Override
    public String toString() {
        return "RiskProfile{" +
                "dealLossPercentage=" + dealLossPercentage +
                ", accountLossPercentage=" + accountLossPercentage +
                ", futuresInAccountPercentage=" + futuresInAccountPercentage +
                ", StockInAccountPercentage=" + StockInAccountPercentage +
                ", riskType=" + riskType +
                '}';
    }
}
