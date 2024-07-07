/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.automation;

import com.stanislav.smart.domain.entities.TimeFrame;

public interface TradingStrategy {

    TimeFrame.Scope timeFrame();

    byte analysing();

    boolean observe();

    void manageDeal();
}
