package com.stanislav.database.hibernate;

import com.stanislav.database.DatabaseRepository;
import org.hibernate.SessionFactory;
import com.stanislav.database.UserPersistence;
import com.stanislav.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service("userPersistence")
public class UserPersistenceHibernate extends HibernatePersistence<User> implements UserPersistence {

    public UserPersistenceHibernate(@Autowired DatabaseRepository databaseRepository) {
        super((SessionFactory) databaseRepository.entityManagerFactory());
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
}