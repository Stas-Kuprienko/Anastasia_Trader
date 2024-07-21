package com.stanislav.smart.entities.criteria;

import com.stanislav.smart.entities.Board;
import com.stanislav.smart.entities.Direction;

public record NewOrderCriteria(String ticker, Board board, double price, int quantity, Direction direction)
        implements OrderCriteria {}
