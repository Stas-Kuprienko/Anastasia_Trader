/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.datasource;

import com.stanislav.trade.entities.orders.Order;
import com.stanislav.trade.entities.user.Account;

import java.util.List;
import java.util.Optional;

public interface OrderDao extends EntityDao<Order, Long> {

    List<Order> findAllByAccount(Account account);

    Optional<Order> findByOrderIdAndAccountId(int orderId, Account account);
}
