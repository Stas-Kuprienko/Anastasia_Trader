package com.stanislav.database.hibernate;

import javax.annotation.PreDestroy;

import com.stanislav.entities.orders.Order;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import com.stanislav.database.AccountPersistence;
import com.stanislav.database.DatabaseRepository;
import com.stanislav.database.OrderPersistence;
import com.stanislav.database.UserPersistence;
import com.stanislav.entities.user.Account;
import com.stanislav.entities.user.User;

public class HibernateRepository implements DatabaseRepository {

    private final SessionFactory sessionFactory;

    private final UserPersistenceHibernate userPersistence;
    private final AccountPersistenceHibernate accountPersistence;
    private final OrderPersistenceHibernate orderPersistence;


    public HibernateRepository(String configuration) {
        this.sessionFactory = new org.hibernate.cfg.Configuration()
                .configure(configuration)
                .addAnnotatedClass(Account.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Order.class)
                .buildSessionFactory();
        this.userPersistence = new UserPersistenceHibernate(sessionFactory);
        this.accountPersistence = new AccountPersistenceHibernate(sessionFactory);
        this.orderPersistence = new OrderPersistenceHibernate(sessionFactory);
    }

    @Override
    public EntityManagerFactory entityManagerFactory() {
        return sessionFactory;
    }

    @Override
    public UserPersistence userPersistence() {
        return userPersistence;
    }

    @Override
    public AccountPersistence accountPersistence() {
        return accountPersistence;
    }

    @Override
    public OrderPersistence orderPersistence() {
        return orderPersistence;
    }

    @PreDestroy
    public void destroy() {
        sessionFactory.close();
    }
}