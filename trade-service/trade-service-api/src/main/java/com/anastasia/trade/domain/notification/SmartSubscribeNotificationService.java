package com.anastasia.trade.domain.notification;

import com.anastasia.trade.entities.notify.SmartSubscribeNotification;

public interface SmartSubscribeNotificationService {

    void sendOut(SmartSubscribeNotification notifyDto);
}
