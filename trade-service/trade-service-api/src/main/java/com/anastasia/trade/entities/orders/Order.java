/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.trade.entities.orders;

import com.anastasia.trade.entities.Board;
import com.anastasia.trade.entities.Broker;
import com.anastasia.trade.entities.Direction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public final class Order implements Serializable {

    private Integer orderId;

    private String clientId;

    private Broker broker;

    private String ticker;

    private Board board;

    private BigDecimal price;

    private long quantity;

    private Direction direction;

    private String status;

    private LocalDateTime created;


    public Order(int orderId, String clientId, Broker broker, String ticker, Board board,
                 BigDecimal price, long quantity, Direction direction, String status, LocalDateTime created) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.broker = broker;
        this.ticker = ticker;
        this.board = board;
        this.price = price;
        this.quantity = quantity;
        this.direction = direction;
        this.status = status;
        this.created = created;
    }

    public Order() {}

    public static OrderBuilder builder() {
        return new Order().new OrderBuilder();
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(orderId, order.orderId) &&
                Objects.equals(clientId, order.clientId) &&
                broker == order.broker &&
                Objects.equals(ticker, order.ticker) &&
                board == order.board;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, clientId, broker, ticker, board);
    }

    @Override
    public String toString() {
        return "Order{" +
                ", orderId=" + orderId +
                ", clientId='" + clientId + '\'' +
                ", broker=" + broker +
                ", ticker='" + ticker + '\'' +
                ", board=" + board +
                ", price=" + price +
                ", quantity=" + quantity +
                ", direction=" + direction +
                ", status='" + status + '\'' +
                '}';
    }

    public final class OrderBuilder {

        private OrderBuilder() {}

        public OrderBuilder orderId(int orderId) {
            Order.this.setOrderId(orderId);
            return this;
        }

        public OrderBuilder clientId(String clientId) {
            Order.this.setClientId(clientId);
            return this;
        }

        public OrderBuilder broker(Broker broker) {
            Order.this.setBroker(broker);
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

        public OrderBuilder quantity(long quantity) {
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

        public OrderBuilder board(Board board) {
            Order.this.setBoard(board);
            return this;
        }

        public OrderBuilder created(LocalDateTime created) {
            Order.this.setCreated(created);
            return this;
        }

        public Order build() {
            return Order.this;
        }
    }
}