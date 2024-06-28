/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.datasource;

import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import java.util.List;

public interface AccountDao extends EntityDao<Account, Long> {

    List<Account> findAllByUser(User user);
}