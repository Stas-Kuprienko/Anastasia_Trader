/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.web.authentication.form;

import com.stanislav.trade.web.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    private final UserDataService userDataService;

    @Autowired
    public MyUserDetailsService(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDataService
                .findUserByLogin(username)
                .map(MyUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
