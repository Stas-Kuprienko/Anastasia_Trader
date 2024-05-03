/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.strategies;

import com.stanislav.smart.domain.analysis.AideSupplier;
import org.springframework.beans.factory.annotation.Autowired;

public class StrategySupplier {

    private final AideSupplier aideSupplier;

    @Autowired
    public StrategySupplier(AideSupplier aideSupplier) {
        this.aideSupplier = aideSupplier;
    }


}
