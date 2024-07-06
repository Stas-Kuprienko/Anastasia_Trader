/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.trading.finam.order_dto;

import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.Direction;
import com.stanislav.trade.entities.Market;
import com.stanislav.trade.entities.orders.Order;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Builder
public final class FinamOrderResponse {

    private int orderNo;
    private int transactionId;
    private String securityCode;
    private String clientId;
    private OrderStatus status;
    private BuySell buySell;
    private String createdAt;
    private double price;
    private int quantity;
    private int balance;
    private String message;
    private String currency;
    private OrderCondition condition;
    private OrderValidBefore validBefore;
    private String acceptedAt;
    private String securityBoard;
    private Market market;


    public FinamOrderResponse(int orderNo, int transactionId, String securityCode, String clientId,
                              OrderStatus status, BuySell buySell, String createdAt,
                              double price, int quantity, int balance, String message, String currency,
                              OrderCondition condition, OrderValidBefore validBefore,
                              String acceptedAt, String securityBoard, Market market) {
        this.orderNo = orderNo;
        this.transactionId = transactionId;
        this.securityCode = securityCode;
        this.clientId = clientId;
        this.status = status;
        this.buySell = buySell;
        this.createdAt = createdAt;
        this.price = price;
        this.quantity = quantity;
        this.balance = balance;
        this.message = message;
        this.currency = currency;
        this.condition = condition;
        this.validBefore = validBefore;
        this.acceptedAt = acceptedAt;
        this.securityBoard = securityBoard;
        this.market = market;
    }

    public FinamOrderResponse() {}


    public Order toOrderClass() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME.withLocale(Locale.of("ru"));
        return Order.builder()
                .orderId(transactionId)
                .clientId(clientId)
                .broker(Broker.Finam)
                .ticker(securityCode)
                .board(Board.valueOf(securityBoard))
                .price(BigDecimal.valueOf(price))
                .quantity(quantity)
                .direction(Direction.parse(buySell))
                .status(status == null ? null : status.toString())
                .created(LocalDateTime.parse(acceptedAt, formatter))
                .build();
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BuySell getBuySell() {
        return buySell;
    }

    public void setBuySell(BuySell buySell) {
        this.buySell = buySell;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public OrderCondition getCondition() {
        return condition;
    }

    public void setCondition(OrderCondition condition) {
        this.condition = condition;
    }

    public OrderValidBefore getValidBefore() {
        return validBefore;
    }

    public void setValidBefore(OrderValidBefore validBefore) {
        this.validBefore = validBefore;
    }

    public String getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(String acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public String getSecurityBoard() {
        return securityBoard;
    }

    public void setSecurityBoard(String securityBoard) {
        this.securityBoard = securityBoard;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public enum OrderStatus {

        None, Active, Matched, Cancelled
    }
}