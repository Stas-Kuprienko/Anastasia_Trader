/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.analysis;

import com.stanislav.smart.domain.entities.TimeFrame;

import java.util.concurrent.ConcurrentHashMap;

public class AideSupplier {

    private final ConcurrentHashMap<Parameter, AnalysisAide> storage;

    public AideSupplier() {
        this.storage = new ConcurrentHashMap<>();
    }



    private record Parameter(String ticker, AnalysisAide aide, TimeFrame.Scope timeFrame) {}
}
