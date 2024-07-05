package com.stanislav.trade.datasource.hibernate;

import com.stanislav.trade.datasource.AccountDao;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Component("accountDao")
public class AccountDaoHibernate implements AccountDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final QueryGenerator jpQuery;


    public AccountDaoHibernate() {
        this.jpQuery = new QueryGenerator();
    }


    @Override
    public List<Account> findAllByUser(Long userId) {
        String param = "user";
        String jpql = jpQuery.nativeSelectAllWhere1Param(Account.class, param);
        var query = entityManager.createQuery(jpql, Account.class);
        query.setParameter(param, userId);
        return query.getResultList();
    }

    @Override
    public Account save(Account account) {
        entityManager.persist(account);
        entityManager.flush();
        return account;
    }

    @Override
    public List<Account> findAll() {
        //TODO make with Page
        return null;
    }

    @Override
    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Account.class, id));
    }

    @Override
    public Optional<Account> update(Long id, Consumer<Account> updating) {
        return Optional.empty();
    }

    @Override
    public void delete(Account object) {

    }
}
