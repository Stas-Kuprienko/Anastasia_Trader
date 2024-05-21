/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.datasource.jpa_repositories;

import com.stanislav.trade.datasource.OrderDao;
import com.stanislav.trade.entities.orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("orderDao")
public interface OrderRepository extends OrderDao, JpaRepository<Order, Long> {

}
