/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.automation;

import com.stanislav.smart.entities.Direction;
import com.stanislav.smart.entities.Stoppable;
import com.stanislav.smart.entities.TimeFrame;

public interface TradeStrategy extends Stoppable {

    TimeFrame.Scope timeFrame();

    double getPrice();

    int getQuantity();

    byte analysing();

    boolean observe();

    void manageDeal();

    Direction getDirection();
}
