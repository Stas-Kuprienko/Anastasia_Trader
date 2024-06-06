package com.stanislav.trade.datasource;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javax.annotation.PreDestroy;
import java.util.Set;

@Configuration
public class DatasourceConfig {

    private EntityManagerFactory entityManagerFactory;

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        org.hibernate.cfg.Configuration config = new org.hibernate.cfg.Configuration().configure();

        ClassPathScanningCandidateComponentProvider componentProvider =
                new ClassPathScanningCandidateComponentProvider(false);

        componentProvider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));

        Set<BeanDefinition> beanDefinitions =
                componentProvider.findCandidateComponents("com.stanislav.trade.entities");

        try {
            for (BeanDefinition bd : beanDefinitions) {
                if (bd instanceof AnnotatedBeanDefinition) {
                    config.addAnnotatedClass(Class.forName(bd.getBeanClassName()));
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return entityManagerFactory = config.buildSessionFactory();
    }

    @PreDestroy
    public void destroy() {
        entityManagerFactory.close();
    }
}