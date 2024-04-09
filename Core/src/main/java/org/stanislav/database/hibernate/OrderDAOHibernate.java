package org.stanislav.database.hibernate;

import org.hibernate.SessionFactory;
import org.stanislav.database.OrderDAO;
import org.stanislav.entities.orders.Direction;
import org.stanislav.entities.orders.Order;
import org.stanislav.entities.user.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

public class OrderDAOHibernate extends HibernateDAO<Order> implements OrderDAO {

    public OrderDAOHibernate(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    @Override
    public Order create(int id, Account account, String ticker, BigDecimal price, int quantity, Direction direction, String status) {
        Order order = Order.builder()
                .id(id)
                .account(account)
                .ticker(ticker)
                .price(price)
                .quantity(quantity)
                .direction(direction)
                .status(status)
                .build();
        saveToDatabase(order);
        return order;
    }

    @Override
    public void save(Order order) {
        saveToDatabase(order);
    }

    @Override
    public List<Order> getAll() {
        return getAllFromDatabase(Order.class);
    }

    @Override
    public Order getById(Object id) {
        return getFromDatabase(Order.class, id);
    }

    @Override
    public Order update(Object id, Consumer<Order> updating) {
        return updateInDatabase(Order.class, id, updating);
    }

    @Override
    public void delete(Order order) {
        deleteFromDatabase(order);
    }
}