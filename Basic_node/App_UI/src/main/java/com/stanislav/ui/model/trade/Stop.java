/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.ui.model.trade;

import com.stanislav.ui.model.Broker;
import com.stanislav.ui.model.Direction;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public final class Stop implements Serializable {

    private int stopId;

    private Broker broker;

    private String clientId;

    private String ticker;

    private int quantity;

    private BigDecimal price;

    private Direction direction;

    private Type type;

    @Builder
    public Stop(int stopId, Broker broker, String clientId, String ticker,
                int quantity, BigDecimal price, Direction direction, Type type) {
        this.stopId = stopId;
        this.broker = broker;
        this.clientId = clientId;
        this.ticker = ticker;
        this.quantity = quantity;
        this.price = price;
        this.direction = direction;
        this.type = type;
    }

    public Stop() {}


    public int getStopId() {
        return stopId;
    }

    public void setStopId(int stopId) {
        this.stopId = stopId;
    }

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        STOP_LOSS,
        TAKE_PROFIT
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stop stop)) return false;
        return stopId == stop.stopId &&
                broker == stop.broker &&
                Objects.equals(clientId, stop.clientId) &&
                Objects.equals(ticker, stop.ticker) &&
                type == stop.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stopId, broker, clientId, ticker, type);
    }

    @Override
    public String toString() {
        return "Stop{" +
                ", stopId=" + stopId +
                ", broker=" + broker +
                ", clientId='" + clientId + '\'' +
                ", ticker='" + ticker + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", direction=" + direction +
                ", type=" + type +
                '}';
    }
}