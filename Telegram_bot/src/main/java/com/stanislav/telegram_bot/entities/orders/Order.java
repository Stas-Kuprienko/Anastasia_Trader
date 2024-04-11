package com.stanislav.telegram_bot.entities.orders;

import com.stanislav.telegram_bot.entities.user.Account;

import java.math.BigDecimal;
import java.util.Objects;

public final class Order {

    private int id;

    private Account account;

    private String ticker;

    private BigDecimal price;

    private int quantity;

    private Direction direction;

    private String status;


    public Order(int id, Account account, String ticker, BigDecimal price, int quantity, Direction direction, String status) {
        this.id = id;
        this.account = account;
        this.ticker = ticker;
        this.price = price;
        this.quantity = quantity;
        this.direction = direction;
        this.status = status;
    }

    public Order() {}

    public static OrderBuilder builder() {
        return new Order().new OrderBuilder();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && quantity == order.quantity &&
                Objects.equals(account, order.account) && Objects.equals(ticker, order.ticker) &&
                Objects.equals(price, order.price) && direction == order.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account.hashCode(), ticker, price, quantity, direction);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
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
            Order.this.setId(id);
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