/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.datasource.hibernate;

import com.stanislav.trade.datasource.TradeOrderDao;
import com.stanislav.trade.entities.orders.Order;
import com.stanislav.trade.entities.user.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
//@Component("orderDao")
public class TradeOrderDaoHibernate implements TradeOrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final QueryGenerator jpQuery;


    public TradeOrderDaoHibernate() {
        jpQuery = new QueryGenerator();
    }


    @Override
    public Order save(Order order) {
        entityManager.persist(order);
        return order;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Order.class, id));
    }

    @Override
    public Optional<Order> update(Long id, Consumer<Order> updating) {
        return Optional.empty();
    }

    @Override
    public void delete(Order order) {

    }

    @Override
    public List<Order> findAllByAccount(Account account) {
        String param = "account";
        String jpql = jpQuery
                .initSelect()
                .fullyFrom()
                .table(Order.class)
                .where(param)
                .build();
        var query = entityManager.createQuery(jpql, Order.class);
        query.setParameter(param, account);
        return query.getResultList();
    }

    @Override
    public Optional<Order> findByOrderIdAndAccountId(int orderId, Account account) {
        String[] parameters = {"orderId", "clientId"};
        String jpql = jpQuery
                .initSelect()
                .fullyFrom()
                .table(Order.class)
                .where(parameters)
                .build();
        var query = entityManager.createQuery(jpql, Order.class);
        query.setParameter(parameters[0], orderId)
                .setParameter(parameters[1], account.getClientId());
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            log.info(e.getMessage());
            return Optional.empty();
        }
    }
}
