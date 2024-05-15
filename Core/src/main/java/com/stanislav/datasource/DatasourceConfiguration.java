package com.stanislav.datasource;

import jakarta.persistence.EntityManagerFactory;

public interface DatasourceConfiguration {

    EntityManagerFactory entityManagerFactory();
}
