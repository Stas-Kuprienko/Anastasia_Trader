package com.stanislav.trade.domain.notification;

import com.stanislav.trade.entities.notify.SmartSubscribeNotification;

public interface SmartSubscribeNotificationService {

    void sendOut(SmartSubscribeNotification notifyDto);
}
