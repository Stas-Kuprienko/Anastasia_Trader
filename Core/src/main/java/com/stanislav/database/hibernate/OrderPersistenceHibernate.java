package com.stanislav.database.hibernate;

import com.stanislav.database.DatabaseRepository;
import com.stanislav.entities.orders.Order;
import org.hibernate.SessionFactory;
import com.stanislav.database.OrderPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service("orderPersistence")
public class OrderPersistenceHibernate extends HibernatePersistence<Order> implements OrderPersistence {

    public OrderPersistenceHibernate(@Autowired DatabaseRepository databaseRepository) {
        super((SessionFactory) databaseRepository.entityManagerFactory());
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