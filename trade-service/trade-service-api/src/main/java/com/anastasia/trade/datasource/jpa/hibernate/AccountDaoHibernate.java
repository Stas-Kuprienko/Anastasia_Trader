package com.anastasia.trade.datasource.jpa.hibernate;

import com.anastasia.trade.datasource.jpa.AccountDao;
import com.anastasia.trade.entities.user.Account;
import com.anastasia.trade.entities.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component("accountDao")
public class AccountDaoHibernate implements AccountDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final QueryGenerator jpQuery;


    public AccountDaoHibernate() {
        this.jpQuery = new QueryGenerator();
    }


    @Override
    public List<Account> findAllByUser(User user) {
        String param = "user";
        String jpql = jpQuery
                .initSelect()
                .fullyFrom()
                .table(Account.class)
                .where(param)
                .build();
        var query = entityManager.createQuery(jpql, Account.class);
        query.setParameter(param, user);
        return query.getResultList();
    }

    @Override
    public List<Account> findByParameters(Map<String, Object> params) {
        if (params.isEmpty()) {
            throw new IllegalArgumentException("parameters map is empty");
        }
        String jpql = jpQuery
                .initSelect()
                .fullyFrom()
                .table(Account.class)
                .where(params.keySet().toArray(new String[]{}))
                .build();
        var query = entityManager.createQuery(jpql, Account.class);
        for (var p : params.entrySet()) {
            query.setParameter(p.getKey(), p.getValue());
        }
        return query.getResultList();
    }

    @Override
    public Account save(Account account) {
        entityManager.persist(account);
        entityManager.flush();
        return account;
    }

    @Override
    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Account.class, id));
    }

    @Override
    public void delete(Account object) {

    }
}