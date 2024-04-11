package org.stanislav.database.hibernate;

import org.hibernate.SessionFactory;
import org.stanislav.database.AccountPersistence;
import org.stanislav.entities.user.Account;
import org.stanislav.entities.user.User;

import java.util.List;
import java.util.function.Consumer;

public class AccountPersistenceHibernate extends HibernatePersistence<Account> implements AccountPersistence {

    public AccountPersistenceHibernate(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Account> getByUser(User user) {
        return null;
    }

    @Override
    public void save(Account account) {
        saveToDatabase(account);
    }

    @Override
    public List<Account> getAll() {
        return getAllFromDatabase(Account.class);
    }

    @Override
    public Account getById(Object id) {
        return getFromDatabase(Account.class, id);
    }

    @Override
    public Account update(Object id, Consumer<Account> updating) {
        return updateInDatabase(Account.class, id, updating);
    }

    @Override
    public void delete(Account account) {
        deleteFromDatabase(account);
    }
}