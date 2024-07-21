/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.telegram_bot.entities.user;

import com.stanislav.telegram_bot.entities.Broker;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public final class Portfolio {

    private String clientId;
    private Broker broker;
    private BigDecimal balance;
    private List<Position> positions;


    public Portfolio() {
        positions = new ArrayList<>();
    }

    public Portfolio(String clientId, Broker broker, BigDecimal balance, List<Position> positions) {
        this.clientId = clientId;
        this.broker = broker;
        this.balance = balance;
        this.positions = positions;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }


    public record Position(String ticker, String market, double price, double totalCost, long quantity, Currency currency, double profit) {}
}
