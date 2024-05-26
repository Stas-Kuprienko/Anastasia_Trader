/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.datasource;

import com.stanislav.trade.entities.user.Account;

import java.util.List;

public interface AccountDao extends EntityDao<Account, Long> {

    List<Account> findAllByUserId(Long id);
}