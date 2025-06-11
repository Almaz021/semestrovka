package ru.itis.fisd.semestrovka.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itis.fisd.semestrovka.entity.orm.User;
import ru.itis.fisd.semestrovka.service.UserService;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService service;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = service.findByUsername(username);
        return new UserDetailsImpl(user);
    }
}
