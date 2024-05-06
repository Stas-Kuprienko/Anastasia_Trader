/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.automation.strategies;

public interface TradingStrategy {

    int getId();

    void analysing(double lastPrice);
}
