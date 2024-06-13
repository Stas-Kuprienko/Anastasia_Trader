package com.stanislav.telegram_bot.datasource.repositories;

import com.stanislav.telegram_bot.entities.user.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDao extends JpaRepository<Account, Long> {

}
