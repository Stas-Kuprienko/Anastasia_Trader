package com.stanislav.datasource.hibernate;

import com.stanislav.datasource.DatasourceConfiguration;
import com.stanislav.entities.orders.Order;
import com.stanislav.entities.user.Account;
import com.stanislav.entities.user.RiskProfile;
import com.stanislav.entities.user.User;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

import javax.annotation.PreDestroy;

public class DatasourceConfigurationHibernate implements DatasourceConfiguration {

    private final SessionFactory sessionFactory;

    public DatasourceConfigurationHibernate(String configuration) {
        this.sessionFactory = new Configuration()
                .configure(configuration)
                .setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy())
                .addAnnotatedClass(Account.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Order.class)
                .addAnnotatedClass(RiskProfile.class)
                .buildSessionFactory();
    }

    @Override
    public EntityManagerFactory entityManagerFactory() {
        return sessionFactory;
    }

    @PreDestroy
    public void destroy() {
        sessionFactory.close();
    }
}