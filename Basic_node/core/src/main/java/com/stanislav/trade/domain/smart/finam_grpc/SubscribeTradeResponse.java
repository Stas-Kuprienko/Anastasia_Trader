package com.stanislav.trade.domain.smart.finam_grpc;

import com.stanislav.trade.domain.notification.SmartSubscribeNotificationService;
import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.Direction;
import com.stanislav.trade.entities.notify.SmartSubscribeNotification;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import stanislav.anastasia.trade.Smart;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class SubscribeTradeResponse implements StreamObserver<Smart.SubscribeTradeResponse> {

    private final SmartSubscribeNotificationService notificationService;


    public SubscribeTradeResponse(SmartSubscribeNotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @Override
    public void onNext(Smart.SubscribeTradeResponse response) {
        try {
            Smart.OrderNotification notification = response.getNotification();

            log.debug(notification.getAllFields().toString());

            SmartSubscribeNotification notifyDto = parse(notification);
            notificationService.sendOut(notifyDto);
        } catch (Exception e) {
            onError(e);
        }
    }

    @Override
    public void onError(Throwable t) {
        log.error(t.getMessage());
    }

    @Override
    public void onCompleted() {
        log.info("subscription completed");
    }


    private SmartSubscribeNotification parse(Smart.OrderNotification notification) {
        List<Smart.Account> accounts = notification.getAccountList();
        Set<SmartSubscribeNotification.AccountDto> accountDtoSet = new HashSet<>();
        for (Smart.Account a : accounts) {
            var dto = new SmartSubscribeNotification.AccountDto(a.getClientId(), Broker.valueOf(a.getBroker()));
            accountDtoSet.add(dto);
        }
        var security = notification.getSecurity();
        SmartSubscribeNotification.SecurityDto securityDto =
                new SmartSubscribeNotification.SecurityDto(security.getTicker(), Board.valueOf(security.getBoard()));
        Direction direction = Direction.parse(notification.getDirection());
        LocalDateTime dateTime = LocalDateTime.parse(notification.getDateTime(), DateTimeFormatter.ISO_DATE_TIME);
        return new SmartSubscribeNotification(accountDtoSet, securityDto, notification.getPrice(), direction, dateTime);
    }
}
