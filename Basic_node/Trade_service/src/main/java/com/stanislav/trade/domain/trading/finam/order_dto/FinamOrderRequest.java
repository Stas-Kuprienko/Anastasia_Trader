/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.trading.finam.order_dto;

import lombok.Builder;

@Builder
public final class FinamOrderRequest {

    private String clientId;
    private String securityBoard;
    private String securityCode;
    private BuySell buySell;
    private long quantity;
    private boolean useCredit;
    private double price;
    private Property property;
    private OrderCondition condition;
    private OrderValidBefore validBefore;

    public FinamOrderRequest(String clientId, String securityBoard, String securityCode,
                             BuySell buySell, long quantity, boolean useCredit, double price,
                             Property property, OrderCondition condition, OrderValidBefore validBefore) {
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

    public BuySell getBuySell() {
        return buySell;
    }

    public void setBuySell(BuySell buySell) {
        this.buySell = buySell;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
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


    public enum Property {
        PutInQueue, CancelBalance, ImmOrCancel
    }

}