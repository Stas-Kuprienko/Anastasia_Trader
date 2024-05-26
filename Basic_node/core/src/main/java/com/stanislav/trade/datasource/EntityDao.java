/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.datasource;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface EntityDao<ENTITY, ID> {

    ENTITY save(ENTITY object);

    List<ENTITY> findAll();

    Optional<ENTITY> findById(ID id);

    ENTITY update(ID id, Consumer<ENTITY> updating);

    void delete(ENTITY object);
}
