/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.datasource.hibernate;

import com.stanislav.trade.datasource.UserDao;
import com.stanislav.trade.entities.user.TelegramChatId;
import com.stanislav.trade.entities.user.User;
import jakarta.persistence.*;
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
    public Optional<User> update(Object id, Consumer<User> updating) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try (entityManager) {
            entityManager.getTransaction().begin();
            String login = (String) id;
            User user = findByLogin(login).orElseThrow(EntityNotFoundException::new);
            updating.accept(user);
            entityManager.getTransaction().commit();
            return Optional.of(user);
        } catch (ClassCastException | PersistenceException e) {
            //TODO log
            return Optional.empty();
        }
    }

    @Override
    public void delete(User object) {

    }

    @Override
    public boolean addTelegramChatId(User user, Long chatId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try (entityManager) {
            TelegramChatId telegramChatId = new TelegramChatId(chatId, user);
            entityManager.getTransaction().begin();
            entityManager.persist(telegramChatId);
            entityManager.getTransaction().commit();
            return true;
        } catch (PersistenceException e) {
            //TODO logs
            return false;
        }
    }

    @Override
    public Optional<TelegramChatId> findTelegramChatId(Long userId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try (entityManager) {
            entityManager.getTransaction().begin();
            String param = "user";
            var query = entityManager.createNativeQuery(
                    generator.nativeSelectAllWhere1Param(TelegramChatId.class, param), TelegramChatId.class);
            query.setParameter(param, userId);
            return Optional.of((TelegramChatId) query.getSingleResult());
        } catch (Exception e) {
            //TODO loggers
            return Optional.empty();
        }
    }
}
