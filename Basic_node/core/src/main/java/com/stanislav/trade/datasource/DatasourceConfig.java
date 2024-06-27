package com.stanislav.trade.datasource;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javax.annotation.PreDestroy;
import java.util.Set;

@Slf4j
@Configuration
public class DatasourceConfig {

    private EntityManagerFactory entityManagerFactory;

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        MetadataSources metadataSources = new MetadataSources(registry);
        ClassPathScanningCandidateComponentProvider componentProvider =
                new ClassPathScanningCandidateComponentProvider(false);

        componentProvider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));

        Set<BeanDefinition> beanDefinitions =
                componentProvider.findCandidateComponents("com.stanislav.trade.entities");

        try {
            for (BeanDefinition bd : beanDefinitions) {
                if (bd instanceof AnnotatedBeanDefinition) {
                    metadataSources.addAnnotatedClass(Class.forName(bd.getBeanClassName()));
                }
            }
        } catch (ClassNotFoundException e) {
            StandardServiceRegistryBuilder.destroy(registry);
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return entityManagerFactory = metadataSources.buildMetadata().buildSessionFactory();
    }

    @PreDestroy
    public void destroy() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}