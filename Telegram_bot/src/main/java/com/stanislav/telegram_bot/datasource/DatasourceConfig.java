package com.stanislav.telegram_bot.datasource;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@PropertySource("classpath:application.properties")
@EnableJpaRepositories(basePackages = "com.stanislav.telegram_bot.datasource.repositories")
@EnableTransactionManagement
public class DatasourceConfig {

    private final Properties databaseProperties;


    public DatasourceConfig(@Value("${database.properties.file}") String fileName) {
        databaseProperties = loadDatabaseProperties(fileName);
    }


    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                databaseProperties.getProperty("connection.url"),
                databaseProperties.getProperty("connection.username"),
                databaseProperties.getProperty("connection.password"));
        dataSource.setDriverClassName(databaseProperties.getProperty("connection.driver_class"));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Autowired DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.MYSQL);

        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan("com.stanislav.telegram_bot.entities");
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactory.setJpaProperties(databaseProperties);
        return entityManagerFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(@Autowired EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }


    private Properties loadDatabaseProperties(String fileName) {
        Properties properties = new Properties();
        try (InputStream inputStream =
                     DatasourceConfig.class.getClassLoader().getResourceAsStream(fileName)) {

            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
