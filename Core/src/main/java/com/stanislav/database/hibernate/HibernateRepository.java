package com.stanislav.database.hibernate;

import com.stanislav.database.DatabaseRepository;
import com.stanislav.entities.orders.Order;
import com.stanislav.entities.user.Account;
import com.stanislav.entities.user.User;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;

import javax.annotation.PreDestroy;

public class HibernateRepository implements DatabaseRepository {

    private final SessionFactory sessionFactory;


    public HibernateRepository(String configuration) {
        this.sessionFactory = new org.hibernate.cfg.Configuration()
                .configure(configuration)
                .addAnnotatedClass(Account.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Order.class)
                .buildSessionFactory();
    }

    @Override
    public EntityManagerFactory entityManagerFactory() {
        return sessionFactory;
    }

    @PreDestroy
    public void destroy() {
        sessionFactory.close();
    }
}