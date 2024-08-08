/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.smart.domain.automation;

import com.anastasia.smart.entities.Direction;
import com.anastasia.smart.entities.Stoppable;
import com.anastasia.smart.entities.TimeFrame;

public interface TradeStrategy extends Stoppable {

    TimeFrame.Scope timeFrame();

    double getPrice();

    int getQuantity();

    byte analysing();

    boolean observe();

    void manageDeal();

    Direction getDirection();
}
