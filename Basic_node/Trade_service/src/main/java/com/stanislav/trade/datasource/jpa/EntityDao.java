/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.datasource.jpa;

import java.util.Optional;
import java.util.function.Consumer;

public interface EntityDao<ENTITY, ID> {

    ENTITY save(ENTITY object);

    Optional<ENTITY> findById(ID id);

    Optional<ENTITY> update(ID id, Consumer<ENTITY> updating);

    void delete(ENTITY object);
}
