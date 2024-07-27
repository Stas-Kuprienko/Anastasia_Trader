/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.datasource.jpa;

import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;

import java.util.List;
import java.util.Map;

public interface AccountDao extends EntityDao<Account, Long> {

    List<Account> findAllByUser(User user);

    List<Account> findByParameters(Map<String, Object> params);
}