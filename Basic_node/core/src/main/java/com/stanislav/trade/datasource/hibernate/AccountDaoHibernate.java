package com.stanislav.trade.datasource.hibernate;

import com.stanislav.trade.datasource.AccountDao;
import com.stanislav.trade.entities.user.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Component("accountDao")
public class AccountDaoHibernate implements AccountDao {

    private final EntityManagerFactory entityManagerFactory;

    private final QueryGenerator generator;


    @Autowired
    public AccountDaoHibernate(EntityManagerFactory entityManagerFactory, QueryGenerator generator) {
        this.entityManagerFactory = entityManagerFactory;
        this.generator = generator;
    }

    @Override
    public List<Account> findAllByUserLogin(String login) {
        return null;
    }

    @Override
    public Account save(Account object) {
        return null;
    }

    @Override
    public List<Account> findAll() {
        return null;
    }

    @Override
    public Optional<Account> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Account update(Long aLong, Consumer<Account> updating) {
        return null;
    }

    @Override
    public void delete(Account object) {

    }
}
