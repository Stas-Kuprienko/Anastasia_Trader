package com.stanislav.trade.datasource.hibernate;

import com.stanislav.trade.datasource.AccountDao;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
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
    public AccountDaoHibernate(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.generator = new QueryGenerator();
    }


    @Override
    public List<Account> findAllByUser(User user) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try (entityManager) {
            entityManager.getTransaction().begin();
            String param = "user";
            String jpql = generator.initSelect().allFrom().table(Account.class).where(param).build();
            var query = entityManager.createQuery(jpql, Account.class);
            query.setParameter(param, user);
            return query.getResultList();
        }
    }

    @Override
    public List<Account> findAllByUserLogin(String login) {
        return null;
    }

    @Override
    public Account save(Account object) {
        var entityManager = entityManagerFactory.createEntityManager();
        try (entityManager) {
            entityManager.getTransaction().begin();
            entityManager.persist(object);
            entityManager.getTransaction().commit();
            return object;
        }
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
    public Optional<Account> update(Object id, Consumer<Account> updating) {
        return Optional.empty();
    }

    @Override
    public void delete(Account object) {

    }
}
