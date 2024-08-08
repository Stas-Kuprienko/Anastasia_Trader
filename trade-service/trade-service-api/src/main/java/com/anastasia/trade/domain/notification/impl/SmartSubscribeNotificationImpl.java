package com.anastasia.trade.domain.notification.impl;

import com.anastasia.trade.domain.notification.SmartSubscribeNotificationService;
import com.anastasia.trade.entities.notify.SmartSubscribeNotification;
import org.springframework.stereotype.Service;

@Service("smartSubscribeNotificationService")
public class SmartSubscribeNotificationImpl implements SmartSubscribeNotificationService {


    @Override
    public void sendOut(SmartSubscribeNotification notifyDto) {
        //TODO
        System.out.println(notifyDto);
    }
}
