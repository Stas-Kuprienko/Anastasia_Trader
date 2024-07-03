package com.stanislav.trade.web.service;

import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;
import java.util.Set;

public interface AccountService {

    Account create(User user, String clientId, String token, String broker);

    Optional<Account> findById(Long id, String login) throws AccessDeniedException;

    Set<Account> findByUser(Long userId);

    Optional<Account> findById(User user, Long id);

    String decodeToken(Account account);

    void delete(Long id);
}
