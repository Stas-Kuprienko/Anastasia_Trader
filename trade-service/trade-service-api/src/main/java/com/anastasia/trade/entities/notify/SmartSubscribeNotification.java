package com.anastasia.trade.entities.notify;

import com.anastasia.trade.entities.Board;
import com.anastasia.trade.entities.Broker;
import com.anastasia.trade.entities.Direction;

import java.time.LocalDateTime;
import java.util.Set;

public record SmartSubscribeNotification(Set<AccountDto> accounts, SecurityDto security,
                                         double price, Direction direction, LocalDateTime dateTime) {

    public record AccountDto(String clientId, Broker broker) {}

    public record SecurityDto(String ticker, Board board) {}
}
