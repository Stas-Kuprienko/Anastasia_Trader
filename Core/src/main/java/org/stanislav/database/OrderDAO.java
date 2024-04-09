package org.stanislav.database;

import org.stanislav.entities.orders.Direction;
import org.stanislav.entities.orders.Order;
import org.stanislav.entities.user.Account;

import java.math.BigDecimal;

public interface OrderDAO extends DAO<Order> {

    Order create(int id, Account account, String ticker, BigDecimal price, int quantity, Direction direction, String status);

}
