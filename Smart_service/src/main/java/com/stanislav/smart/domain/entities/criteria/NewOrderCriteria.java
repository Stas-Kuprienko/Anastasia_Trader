package com.stanislav.smart.domain.entities.criteria;

import com.stanislav.smart.domain.entities.Board;
import com.stanislav.smart.domain.entities.Direction;

public record NewOrderCriteria(String ticker, Board board, double price, int quantity, Direction direction)
        implements OrderCriteria {}
