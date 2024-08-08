/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.smart.domain.analysis;

import com.anastasia.smart.entities.Board;
import com.anastasia.smart.entities.TimeFrame;

public interface AnalysisAideSupplier<AA extends AnalysisAide> {

    AA simpleMovingAverage(String ticker, Board board, TimeFrame.Scope timeFrame, int period);
}
