/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.automation.grpc_impl;

import com.stanislav.smart.domain.automation.TradingStrategy;
import com.stanislav.smart.domain.automation.TradingStrategySupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Component
public class StrategiesDispatcher {

    private final HashMap<String, TradingStrategySupplier<? extends TradingStrategy>> strategyMap;

    @Autowired
    public StrategiesDispatcher(List<TradingStrategySupplier<? extends TradingStrategy>> strategySuppliers) {
        strategyMap = new HashMap<>();
        for (var supplier : strategySuppliers) {
            strategyMap.put(supplier.getName(), supplier);
        }
    }

    public TradingStrategySupplier<? extends TradingStrategy> getStrategySupplier(String name) {
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