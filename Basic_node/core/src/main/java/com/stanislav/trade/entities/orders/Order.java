/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.entities.orders;

import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Direction;
import com.stanislav.trade.entities.user.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "orders")
public final class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account", nullable = false)
    @JsonIgnore
    private Account account;

    private String ticker;

    @Enumerated(EnumType.STRING)
    private Board board;

    private BigDecimal price;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private Direction direction;

    private String status;


    public Order(long id, int orderId, Account account, String ticker, Board board,
                 BigDecimal price, int quantity, Direction direction, String status) {
        this.id = id;
        this.orderId = orderId;
        this.account = account;
        this.ticker = ticker;
        this.board = board;
        this.price = price;
        this.quantity = quantity;
        this.direction = direction;
        this.status = status;
    }

    public Order() {}

    public static OrderBuilder builder() {
        return new Order().new OrderBuilder();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + orderId +
                ", account='" + account.getClientId() + '\'' +
                ", ticker='" + ticker + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", direction=" + direction +
                '}';
    }


    public final class OrderBuilder {

        private OrderBuilder() {}

        public OrderBuilder id(int id) {
            Order.this.setOrderId(id);
            return this;
        }

        public OrderBuilder account(Account account) {
            Order.this.setAccount(account);
            return this;
        }

        public OrderBuilder ticker(String ticker) {
            Order.this.setTicker(ticker);
            return this;
        }

        public OrderBuilder price(BigDecimal price) {
            Order.this.setPrice(price);
            return this;
        }

        public OrderBuilder quantity(int quantity) {
            Order.this.setQuantity(quantity);
            return this;
        }

        public OrderBuilder direction(Direction direction) {
            Order.this.setDirection(direction);
            return this;
        }

        public OrderBuilder status(String status) {
            Order.this.setStatus(status);
            return this;
        }

        public Order build() {
            return Order.this;
        }
    }
}