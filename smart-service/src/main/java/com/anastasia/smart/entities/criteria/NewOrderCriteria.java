package com.anastasia.smart.entities.criteria;

import com.anastasia.smart.entities.Board;
import com.anastasia.smart.entities.Direction;

public record NewOrderCriteria(String ticker, Board board, double price, int quantity, Direction direction)
        implements OrderCriteria {}
