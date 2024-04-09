package org.stanislav.database.hibernate;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.stanislav.database.AccountDAO;
import org.stanislav.database.OrderDAO;
import org.stanislav.database.DatabaseRepository;
import org.stanislav.database.UserDAO;
import org.stanislav.entities.orders.Order;
import org.stanislav.entities.user.Account;
import org.stanislav.entities.user.User;

@Repository("hibernateRepository")
public class HibernateRepository implements DatabaseRepository {

    private static final String CONFIGURATION = "hibernate.cfg.xml";

    private final SessionFactory sessionFactory;

    private final UserDAOHibernate userRepository;
    private final AccountDAOHibernate accountRepository;
    private final OrderDAOHibernate orderRepository;


    public HibernateRepository() {
        this.sessionFactory = new org.hibernate.cfg.Configuration()
                .configure(CONFIGURATION)
                .addAnnotatedClass(Account.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Order.class)
                .buildSessionFactory();
        this.userRepository = new UserDAOHibernate(sessionFactory);
        this.accountRepository = new AccountDAOHibernate(sessionFactory);
        this.orderRepository = new OrderDAOHibernate(sessionFactory);
    }

    @Override
    public EntityManagerFactory entityManagerFactory() {
        return sessionFactory;
    }

    @Override
    public UserDAO userRepository() {
        return userRepository;
    }

    @Override
    public AccountDAO accountRepository() {
        return accountRepository;
    }

    @Override
    public OrderDAO orderRepository() {
        return orderRepository;
    }
}