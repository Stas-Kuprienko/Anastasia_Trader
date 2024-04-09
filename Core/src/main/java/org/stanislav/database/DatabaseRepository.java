package org.stanislav.database;

import jakarta.persistence.EntityManagerFactory;

public interface DatabaseRepository {

    EntityManagerFactory entityManagerFactory();

    UserDAO userRepository();

    AccountDAO accountRepository();

    OrderDAO orderRepository();
}
