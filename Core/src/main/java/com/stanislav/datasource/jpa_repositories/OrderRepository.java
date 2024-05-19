package com.stanislav.datasource.jpa_repositories;

import com.stanislav.datasource.OrderDao;
import com.stanislav.entities.orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends OrderDao, JpaRepository<Order, Long> {

}
