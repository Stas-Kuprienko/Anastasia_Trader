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
    private FinamBuySell buySell;
    private long quantity;
    private boolean useCredit;
    private double price;
    private Property property;
    private FinamOrderCondition condition;
    private FinamOrderValidBefore validBefore;

    public FinamOrderRequest(String clientId, String securityBoard, String securityCode,
                             FinamBuySell buySell, long quantity, boolean useCredit, double price,
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