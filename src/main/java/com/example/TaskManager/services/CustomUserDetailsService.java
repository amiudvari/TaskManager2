package com.example.TaskManager.services;

import com.example.TaskManager.daos.UserDao;
import com.example.TaskManager.models.User;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to load users for the auth system. It authenticates, and issues tokens.
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {
    private UserDao userDao;

    public CustomUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }


        JwtUser jwtUser = new JwtUser();

        jwtUser.setUsername(user.getUsername());
        jwtUser.setPassword(user.getPassword());

        List<String> roles = userDao.getRolesForUsers(user.getUsername());
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        jwtUser.setAuthorities(authorities);
        jwtUser.setAccountNonExpired(true);
        jwtUser.setAccountNonLocked(true);
        jwtUser.setApiAccessAllowed(true);
        jwtUser.setCredentialsNonExpired(true);
        jwtUser.setEnabled(true);

        return jwtUser;
    }
}
