/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.datasource.hibernate;

import com.stanislav.trade.datasource.UserDao;
import com.stanislav.trade.entities.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Component("userDao")
public class UserDaoHibernate implements UserDao {

    @PersistenceContext
    private EntityManagerFactory entityManagerFactory;


    @Override
    public User save(User user) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            return user;
        }
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public User update(Long aLong, Consumer<User> updating) {
        return null;
    }

    @Override
    public void delete(User object) {

    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.empty();
    }
}
