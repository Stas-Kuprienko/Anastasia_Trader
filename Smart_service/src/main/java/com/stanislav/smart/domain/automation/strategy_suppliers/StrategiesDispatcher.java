/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.automation.strategy_suppliers;

import com.stanislav.smart.domain.automation.TradeStrategy;
import com.stanislav.smart.domain.automation.TradeStrategySupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Component
public class StrategiesDispatcher {

    private final HashMap<String, TradeStrategySupplier<? extends TradeStrategy>> strategyMap;

    @Autowired
    public StrategiesDispatcher(List<TradeStrategySupplier<? extends TradeStrategy>> strategySuppliers) {
        strategyMap = new HashMap<>();
        for (var supplier : strategySuppliers) {
            strategyMap.put(supplier.getName(), supplier);
        }
    }

    public TradeStrategySupplier<? extends TradeStrategy> getStrategySupplier(String name) {
        var supplier = strategyMap.get(name);
        if (supplier == null) {
            throw new IllegalArgumentException("argument = " + name);
        }
        return supplier;
    }

    public Set<String> strategiesNameList() {
        return strategyMap.keySet();
    }
}