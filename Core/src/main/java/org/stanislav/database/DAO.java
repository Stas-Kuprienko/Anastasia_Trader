package org.stanislav.database;

import java.util.List;
import java.util.function.Consumer;

public interface DAO<ENTITY> {

    void save(ENTITY object);

    List<ENTITY> getAll();

    ENTITY getById(Object id);

    ENTITY update(Object id, Consumer<ENTITY> updating);

    void delete(ENTITY object);
}
