package com.stanislav.ui.model.trade;

import java.time.LocalDateTime;

public record NewOrderForm(String ticker,
                           String board,
                           double price,
                           int quantity,
                           String direction,
                           boolean cancelUnfulfilled,
                           boolean isTillCancel,
                           LocalDateTime delayTime) {}
