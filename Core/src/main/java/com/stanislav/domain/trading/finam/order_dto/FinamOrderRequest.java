package com.stanislav.domain.trading.finam.order_dto;

import com.stanislav.database.AccountPersistence;
import com.stanislav.entities.Direction;
import com.stanislav.entities.orders.Order;
import lombok.Builder;
import com.stanislav.entities.user.Account;

import java.math.BigDecimal;

@Builder
public final class FinamOrderRequest {

    private String clientId;
    private String securityBoard;
    private String securityCode;
    private FinamBuySell buySell;
    private int quantity;
    private boolean useCredit;
    private double price;
    private Property property;
    private FinamOrderCondition condition;
    private FinamOrderValidBefore validBefore;

    public FinamOrderRequest(String clientId, String securityBoard, String securityCode,
                             FinamBuySell buySell, int quantity, boolean useCredit, double price,
                             Property property, FinamOrderCondition condition, FinamOrderValidBefore validBefore) {
        this.clientId = clientId;
        this.securityBoard = securityBoard;
        this.securityCode = securityCode;
        this.buySell = buySell;
        this.quantity = quantity;
        this.useCredit = useCredit;
        this.price = price;
        this.property = property;
        this.condition = condition;
        this.validBefore = validBefore;
    }


    public Order toOrderClass(AccountPersistence accountPersistence) {
        Account account = accountPersistence.getById(clientId);
        return Order.builder().account(account)
                .ticker(securityCode)
                .price(BigDecimal.valueOf(price))
                .quantity(quantity)
                .direction(Direction.parse(buySell))
                .build();
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecurityBoard() {
        return securityBoard;
    }

    public void setSecurityBoard(String securityBoard) {
        this.securityBoard = securityBoard;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public FinamBuySell getBuySell() {
        return buySell;
    }

    public void setBuySell(FinamBuySell buySell) {
        this.buySell = buySell;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isUseCredit() {
        return useCredit;
    }

    public void setUseCredit(boolean useCredit) {
        this.useCredit = useCredit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
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


    public enum Property {
        PutInQueue, CancelBalance, ImmOrCancel
    }

}