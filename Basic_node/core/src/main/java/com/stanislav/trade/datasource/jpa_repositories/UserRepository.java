/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.datasource.jpa_repositories;

import com.stanislav.trade.datasource.UserDao;
import com.stanislav.trade.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public interface UserRepository extends UserDao, JpaRepository<User, Long> {

}