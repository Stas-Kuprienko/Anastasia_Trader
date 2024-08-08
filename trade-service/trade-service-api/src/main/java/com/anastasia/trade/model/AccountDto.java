package com.anastasia.trade.model;

public record AccountDto(Long id, Long userId, String clientId, String broker, String token, RiskProfileDto riskProfile) {

    public record RiskProfileDto() {}
}
