/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.web.authentication;

import com.stanislav.trade.datasource.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    @Autowired
    public MyUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao
                .findByLogin(username)
                .map(MyUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
