/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.automation;

import com.stanislav.smart.domain.entities.Direction;
import com.stanislav.smart.domain.entities.TimeFrame;

public interface TradeStrategy {

    TimeFrame.Scope timeFrame();

    double getPrice();

    int getQuantity();

    byte analysing();

    boolean observe();

    void manageDeal();

    Direction getDirection();
}
