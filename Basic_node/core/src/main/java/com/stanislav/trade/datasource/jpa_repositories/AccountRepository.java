/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.datasource.jpa_repositories;

import com.stanislav.trade.datasource.AccountDao;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("accountDao")
public interface AccountRepository extends AccountDao, JpaRepository<Account, Long> {

    @Override
    List<Account> findAllByUser(User user);
}
