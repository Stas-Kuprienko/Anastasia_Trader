/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.analysis;

import com.stanislav.smart.domain.analysis.AnalysisAide;
import com.stanislav.smart.domain.entities.Board;
import com.stanislav.smart.domain.entities.TimeFrame;

public interface AnalysisAideSupplier<AA extends AnalysisAide> {

    AA simpleMovingAverage(String ticker, Board board, TimeFrame.Scope timeFrame, int period);
}
