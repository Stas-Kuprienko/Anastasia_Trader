package com.stanislav.smart.domain.automation;

import com.stanislav.smart.entities.Stoppable;
import stanislav.anastasia.trade.Smart;

public interface Drone extends Runnable, Stoppable {

    void addAccount(Smart.Account account);

    void removeAccount(Smart.Account account);

    void launch();
}
