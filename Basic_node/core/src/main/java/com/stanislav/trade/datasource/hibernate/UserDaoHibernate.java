/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.datasource.hibernate;

import com.stanislav.trade.datasource.UserDao;
import com.stanislav.trade.entities.user.TelegramChatId;
import com.stanislav.trade.entities.user.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Component("userDao")
public class UserDaoHibernate implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final QueryGenerator jpQuery;


    public UserDaoHibernate() {
        this.jpQuery = new QueryGenerator();
    }


    @Override
    public User save(User user) {
        entityManager.persist(user);
        entityManager.flush();
        return entityManager.find(User.class, user.getId());
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        String param = "login";
        String jpql = jpQuery
                .initSelect()
                .fullyFrom()
                .table(User.class)
                .where(param)
                .build();
        try {
            return Optional.of(entityManager
                    .createQuery(jpql, User.class)
                    .setParameter(param, login)
                    .getSingleResult());
        } catch (NoResultException e) {
            log.info(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> update(Long id, Consumer<User> updating) {
        Optional<User> user = Optional.ofNullable(entityManager.find(User.class, id));
        user.ifPresent(updating);
        return user;
    }

    @Override
    public void delete(User user) {
        entityManager.remove(user);
    }

    @Override
    public boolean addTelegramChatId(User user, Long chatId) {
        TelegramChatId chat = new TelegramChatId(chatId, user);
        try {
            entityManager.persist(chat);
            return true;
        } catch (EntityExistsException e) {
            log.info(e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<TelegramChatId> findTelegramChatId(Long userId) {
        String param = "user";
        var query = entityManager.createNativeQuery(
                jpQuery.nativeSelectAllWhere1Param(TelegramChatId.class, param),
                TelegramChatId.class);
        query.setParameter(param, userId);
        return Optional.of((TelegramChatId) query.getSingleResult());
    }
}