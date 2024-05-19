/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.datasource.jpa_repositories;

import com.stanislav.trade.datasource.AccountDao;
import com.stanislav.trade.entities.user.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends AccountDao, JpaRepository<Account, Long> {

}
