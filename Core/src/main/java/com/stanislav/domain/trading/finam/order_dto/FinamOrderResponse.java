package com.stanislav.domain.trading.finam.order_dto;

import com.stanislav.datasource.AccountDao;
import com.stanislav.entities.Market;
import com.stanislav.entities.Direction;
import com.stanislav.entities.orders.Order;
import lombok.Builder;
import com.stanislav.entities.user.Account;

import java.math.BigDecimal;
import java.util.Optional;

@Builder
public final class FinamOrderResponse {

    private int orderNo;
    private int transactionId;
    private String securityCode;
    private String clientId;
    private OrderStatus status;
    private FinamBuySell buySell;
    private String createdAt;
    private double price;
    private int quantity;
    private int balance;
    private String message;
    private String currency;
    private FinamOrderCondition condition;
    private FinamOrderValidBefore validBefore;
    private String acceptedAt;
    private String securityBoard;
    private Market market;


    public FinamOrderResponse(int orderNo, int transactionId, String securityCode, String clientId,
                              OrderStatus status, FinamBuySell buySell, String createdAt,
                              double price, int quantity, int balance, String message, String currency,
                              FinamOrderCondition condition, FinamOrderValidBefore validBefore,
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


    public Order toOrderClass(AccountDao accountPersistence) {
        return Order.builder()
                .id(transactionId)
                //TODO!
                .account(null)
                .ticker(securityCode)
                .price(BigDecimal.valueOf(price))
                .quantity(quantity)
                .direction(Direction.parse(buySell))
                .status(status.toString())
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

    public FinamBuySell getBuySell() {
        return buySell;
    }

    public void setBuySell(FinamBuySell buySell) {
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

    public FinamOrderCondition getCondition() {
        return condition;
    }

    public void setCondition(FinamOrderCondition condition) {
        this.condition = condition;
    }

    public FinamOrderValidBefore getValidBefore() {
        return validBefore;
    }

    public void setValidBefore(FinamOrderValidBefore validBefore) {
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