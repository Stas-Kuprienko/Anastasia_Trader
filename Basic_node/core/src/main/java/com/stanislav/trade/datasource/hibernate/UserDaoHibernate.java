/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.datasource.hibernate;

import com.stanislav.trade.datasource.UserDao;
import com.stanislav.trade.entities.user.User;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import static com.stanislav.trade.datasource.hibernate.QueryGenerator.*;

@Component("userDao")
public class UserDaoHibernate implements UserDao {

    private final EntityManagerFactory entityManagerFactory;

    private final String findAll;
    private final String findBy;


    @Autowired
    public UserDaoHibernate(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.findAll = String.format(Native.selectFrom.q, "user");
        this.findBy = String.format(Native.selectFromWhere.q, "user", "%s", "%s");
    }


    @Override
    public User save(User user) {
        Session session = (Session) entityManagerFactory.createEntityManager();
        try (session) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
            return user;
        }
    }

    @Override
    public List<User> findAll() {
        Session session = (Session) entityManagerFactory.createEntityManager();
        try (session) {
            session.beginTransaction();
            return session.createQuery(findAll, User.class).getResultList();
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        Session session = (Session) entityManagerFactory.createEntityManager();
        try (session) {
            session.beginTransaction();
            return Optional.of(session.find(User.class, id));
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        Session session = (Session) entityManagerFactory.createEntityManager();
        try (session) {
            session.beginTransaction();
            String param = "login";
            var query = session.createNativeQuery(String.format(findBy, param, param), User.class);
            query.setParameter(param, login);
            return Optional.of(query.getSingleResult());
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
