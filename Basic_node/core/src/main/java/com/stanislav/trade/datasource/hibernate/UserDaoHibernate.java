/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.datasource.hibernate;

import com.stanislav.trade.datasource.UserDao;
import com.stanislav.trade.entities.user.TelegramChatId;
import com.stanislav.trade.entities.user.User;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Component("userDao")
public class UserDaoHibernate implements UserDao {

    private final SessionFactory sessionFactory;

    private final QueryGenerator generator;


    @Autowired
    public UserDaoHibernate(EntityManagerFactory entityManagerFactory) {
        try {
            this.sessionFactory = (SessionFactory) entityManagerFactory;
        } catch (ClassCastException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        this.generator = new QueryGenerator();
    }


    @Override
    public User save(User user) {
        sessionFactory.inTransaction(s -> s.persist(user));
        return user;
    }

    @Override
    public List<User> findAll() {
        Session session = sessionFactory.getCurrentSession();
        try (session) {
            session.beginTransaction();
            String jpql = generator.initSelect().allFrom().table(User.class).build();
            return session
                    .createQuery(jpql,User.class)
                    .getResultList();
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        try (session) {
            session.beginTransaction();
            return Optional.of(session.find(User.class, id));
        } catch (NoResultException e) {
            log.debug(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        Session session = sessionFactory.getCurrentSession();
        try (session) {
            session.beginTransaction();
            String param = "login";
            String jpql = generator.initSelect().allFrom().table(User.class).where(param).build();
            var query = session.createQuery(jpql, User.class);
            query.setParameter(param, login);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            log.debug(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> update(Object id, Consumer<User> updating) {
        Session session = sessionFactory.getCurrentSession();
        try (session) {
            session.beginTransaction();
            String login = (String) id;
            User user = findByLogin(login).orElseThrow(EntityNotFoundException::new);
            updating.accept(user);
            session.getTransaction().commit();
            return Optional.of(user);
        } catch (ClassCastException | PersistenceException e) {
            log.warn(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void delete(User object) {

    }

    @Override
    public boolean addTelegramChatId(User user, Long chatId) {
        try {
            TelegramChatId telegramChatId = new TelegramChatId(chatId, user);
            sessionFactory.inTransaction(s -> s.persist(telegramChatId));
            return true;
        } catch (PersistenceException e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<TelegramChatId> findTelegramChatId(Long userId) {
        Session session = sessionFactory.getCurrentSession();
        try (session) {
            session.beginTransaction();
            String param = "user";
            var query = session.createNativeQuery(
                    generator.nativeSelectAllWhere1Param(TelegramChatId.class, param), TelegramChatId.class);
            query.setParameter(param, userId);
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            log.warn(e.getMessage());
            return Optional.empty();
        }
    }
}