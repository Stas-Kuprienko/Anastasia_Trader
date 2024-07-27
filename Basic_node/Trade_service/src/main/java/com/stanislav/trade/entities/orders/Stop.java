/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.entities.orders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stanislav.trade.entities.Direction;
import com.stanislav.trade.entities.user.Account;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "stop")
public final class Stop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int stopId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnore
    private Account account;

    private String ticker;

    private int quantity;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Direction direction;

    @Enumerated(EnumType.STRING)
    private Type type;


    public Stop(long id, int stopId, Account account, String ticker,
                int quantity, BigDecimal price, Direction direction, Type type) {
        this.id = id;
        this.stopId = stopId;
        this.account = account;
        this.ticker = ticker;
        this.quantity = quantity;
        this.price = price;
        this.direction = direction;
        this.type = type;
    }

    public Stop() {}


    public static StopBuilder builder() {
        return new Stop().new StopBuilder();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStopId() {
        return stopId;
    }

    public void setStopId(int stopId) {
        this.stopId = stopId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stop stop)) return false;
        return id == stop.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Stop{" +
                "id=" + id +
                ", stopId=" + stopId +
                ", account='" + account.getClientId() + '\'' +
                ", ticker='" + ticker + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", direction=" + direction +
                ", type=" + type +
                '}';
    }

    public enum Type {
        STOP_LOSS,
        TAKE_PROFIT
    }


    public final class StopBuilder {

        private StopBuilder() {}

        public StopBuilder id(long id) {
            Stop.this.setId(id);
            return this;
        }

        public StopBuilder stopId(int stopId) {
            Stop.this.setStopId(stopId);
            return this;
        }

        public StopBuilder account(Account account) {
            Stop.this.setAccount(account);
            return this;
        }

        public StopBuilder ticker(String ticker) {
            Stop.this.setTicker(ticker);
            return this;
        }

        public StopBuilder quantity(int quantity) {
            Stop.this.setQuantity(quantity);
            return this;
        }

        public StopBuilder price(BigDecimal price) {
            Stop.this.setPrice(price);
            return this;
        }

        public StopBuilder direction(Direction direction) {
            Stop.this.setDirection(direction);
            return this;
        }

        public StopBuilder type(Type type) {
            Stop.this.setType(type);
            return this;
        }

        public Stop build() {
            return Stop.this;
        }
    }
}