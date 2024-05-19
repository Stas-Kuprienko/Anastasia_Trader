package com.stanislav.datasource.jpa_repositories;

import com.stanislav.datasource.AccountDao;
import com.stanislav.entities.user.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends AccountDao, JpaRepository<Account, Long> {

}
