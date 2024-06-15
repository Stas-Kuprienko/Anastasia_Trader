/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.datasource.hibernate;

import com.stanislav.trade.datasource.UserDao;
import com.stanislav.trade.entities.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Component("userDao")
public class UserDaoHibernate implements UserDao {

    private final EntityManagerFactory entityManagerFactory;

    private final QueryGenerator generator;


    @Autowired
    public UserDaoHibernate(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.generator = new QueryGenerator();
    }


    @Override
    public User save(User user) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try (entityManager) {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            return user;
        }
    }

    @Override
    public List<User> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try (entityManager) {
            entityManager.getTransaction().begin();
            String jpql = generator.initSelect().allFrom().table(User.class).build();
            return entityManager
                    .createQuery(jpql,User.class)
                    .getResultList();
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try (entityManager) {
            entityManager.getTransaction().begin();
            return Optional.of(entityManager.find(User.class, id));
        } catch (NoResultException e) {
            // TODO log
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try (entityManager) {
            entityManager.getTransaction().begin();
            String param = "login";
            String jpql = generator.initSelect().allFrom().table(User.class).where(param).build();
            var query = entityManager.createQuery(jpql, User.class);
            query.setParameter(param, login);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            // TODO log
            return Optional.empty();
        }
    }

    @Override
    public User update(Long id, Consumer<User> updating) {
        return null;
    }

    @Override
    public void delete(User object) {

    }
}
