/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.trade.datasource.jpa;

import java.util.Optional;
import java.util.function.Consumer;

public interface EntityDao<ENTITY, ID> {

    ENTITY save(ENTITY object);

    Optional<ENTITY> findById(ID id);

    void delete(ENTITY object);
}
