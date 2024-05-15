package com.stanislav.datasource.hibernate;

import com.stanislav.datasource.DatasourceConfiguration;
import org.hibernate.SessionFactory;
import com.stanislav.datasource.AccountPersistence;
import com.stanislav.entities.user.Account;
import com.stanislav.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service("accountPersistence")
public class AccountPersistenceHibernate extends HibernatePersistence<Account> implements AccountPersistence {

    public AccountPersistenceHibernate(@Autowired DatasourceConfiguration datasourceConfiguration) {
        super((SessionFactory) datasourceConfiguration.entityManagerFactory());
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