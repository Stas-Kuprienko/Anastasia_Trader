/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.automation.strategies;

import com.stanislav.smart.domain.analysis.technical.AnalysisAideSupplier;
import com.stanislav.smart.domain.automation.strategies.impl.MovingAverageCrossingStrategy;
import com.stanislav.smart.domain.entities.TimeFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StrategyManager {

    private final AnalysisAideSupplier analysisAideSupplier;

    @Autowired
    public StrategyManager(AnalysisAideSupplier analysisAideSupplier) {
        this.analysisAideSupplier = analysisAideSupplier;
    }

    //TODO !!!
//    public MovingAverageCrossingStrategy movingAverageCrossingStrategy(String ticker, TimeFrame.Scope timeFrame, int period) {
//        analysisAideSupplier.simpleMovingAverage()
//    }
}
