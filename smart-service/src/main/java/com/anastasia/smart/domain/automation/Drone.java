package com.anastasia.smart.domain.automation;

import com.anastasia.smart.entities.Stoppable;
import com.anastasia.trade.Smart;

public interface Drone extends Runnable, Stoppable {

    void addAccount(Smart.Account account);

    void removeAccount(Smart.Account account);

    void launch();
}
