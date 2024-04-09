package org.stanislav.database.hibernate;

import org.hibernate.SessionFactory;
import org.stanislav.database.UserDAO;
import org.stanislav.entities.user.User;

import java.util.List;
import java.util.function.Consumer;

public class UserDAOHibernate extends HibernateDAO<User> implements UserDAO {

    public UserDAOHibernate(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void save(User user) {
        saveToDatabase(user);
    }

    @Override
    public User getById(Object login) {
        return getFromDatabase(User.class, login);
    }

    @Override
    public User update(Object id, Consumer<User> updating) {
        return updateInDatabase(User.class, id, updating);
    }

    @Override
    public List<User> getAll() {
        return getAllFromDatabase(User.class);
    }

    @Override
    public void delete(User user) {
        deleteFromDatabase(user);
    }

    @Override
    public User create(String name, String login, String password) {
        User user = new User(login, password, name);
        saveToDatabase(user);
        return user;
    }
}