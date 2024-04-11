package org.stanislav.database.hibernate;

import org.hibernate.SessionFactory;
import org.stanislav.database.OrderPersistence;
import org.stanislav.entities.orders.Order;

import java.util.List;
import java.util.function.Consumer;

public class OrderPersistenceHibernate extends HibernatePersistence<Order> implements OrderPersistence {

    public OrderPersistenceHibernate(SessionFactory sessionFactory) {
        super(sessionFactory);
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