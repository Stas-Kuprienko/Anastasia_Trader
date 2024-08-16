/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.trade.entities.orders;

import com.anastasia.trade.entities.Board;
import com.anastasia.trade.entities.Broker;
import com.anastasia.trade.entities.Direction;
import java.math.BigDecimal;
import java.util.Objects;

public final class Stop {

    private int stopId;
    private String clientId;
    private Broker broker;
    private String ticker;
    private Board board;
    private int quantity;
    private BigDecimal price;
    private Direction direction;
    private Type type;


    public Stop(int stopId, String clientId, Broker broker, String ticker, Board board,
                int quantity, BigDecimal price, Direction direction, Type type) {
        this.stopId = stopId;
        this.clientId = clientId;
        this.broker = broker;
        this.ticker = ticker;
        this.board = board;
        this.quantity = quantity;
        this.price = price;
        this.direction = direction;
        this.type = type;
    }

    public Stop() {}


    public static StopBuilder builder() {
        return new Stop().new StopBuilder();
    }


    public int getStopId() {
        return stopId;
    }

    public void setStopId(int stopId) {
        this.stopId = stopId;
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
        return stopId == stop.stopId &&
                Objects.equals(clientId, stop.clientId) &&
                broker == stop.broker &&
                Objects.equals(ticker, stop.ticker) &&
                board == stop.board &&
                direction == stop.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stopId, clientId, broker, ticker, board, direction);
    }



    public enum Type {
        STOP_LOSS,
        TAKE_PROFIT
    }


    public final class StopBuilder {

        private StopBuilder() {}

        public StopBuilder stopId(int stopId) {
            Stop.this.setStopId(stopId);
            return this;
        }

        public StopBuilder clientId(String clientId) {
            Stop.this.setClientId(clientId);
            return this;
        }

        public StopBuilder broker(Broker broker) {
            Stop.this.setBroker(broker);
            return this;
        }

        public StopBuilder ticker(String ticker) {
            Stop.this.setTicker(ticker);
            return this;
        }

        public StopBuilder board(Board board) {
            Stop.this.setBoard(board);
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