package org.stanislav.database;

import jakarta.persistence.EntityManagerFactory;

public interface DatabaseRepository {

    EntityManagerFactory entityManagerFactory();

    UserPersistence userPersistence();

    AccountPersistence accountPersistence();

    OrderPersistence orderPersistence();
}
