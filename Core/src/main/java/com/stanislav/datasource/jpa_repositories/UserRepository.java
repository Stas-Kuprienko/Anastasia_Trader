package com.stanislav.datasource.jpa_repositories;

import com.stanislav.datasource.UserDao;
import com.stanislav.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public interface UserRepository extends UserDao, JpaRepository<User, Long> {

}