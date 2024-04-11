package com.stanislav.database.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.function.Consumer;

public abstract class HibernatePersistence<ENTITY> {

    protected final SessionFactory sessionFactory;

    public HibernatePersistence(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    protected void saveToDatabase(ENTITY entity) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        }
    }

    protected List<ENTITY> getAllFromDatabase(Class<ENTITY> entityClass) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            String query = String.format(Query.SELECT_ALL.value, entityClass.getSimpleName());
            List<ENTITY> list = session.createQuery(query, entityClass).getResultList();
            session.getTransaction().commit();
            return list;
        }
    }

    protected ENTITY getFromDatabase(Class<ENTITY> entityClass, Object key) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            ENTITY entity = session.get(entityClass, key);
            session.getTransaction().commit();
            return entity;
        }
    }

    protected ENTITY updateInDatabase(Class<ENTITY> entityClass, Object key, Consumer<ENTITY> updating) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            ENTITY entity = session.get(entityClass, key);
            updating.accept(entity);
            session.getTransaction().commit();
            return entity;
        }
    }

    protected void deleteFromDatabase(ENTITY entity) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            session.remove(entity);
            session.getTransaction().commit();
        }
    }

    enum Query {

        SELECT_ALL("FROM %s");

        final String value;

        Query(String value) {
            this.value = value;
        }
    }
}