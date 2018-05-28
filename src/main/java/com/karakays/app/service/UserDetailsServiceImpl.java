package com.karakays.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.karakays.app.domain.UserCredentials;
import com.karakays.app.repository.UserCredentialsRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    private UserCredentialsRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredentials credentials = repository.findByUsername(username);
        
//        UserDetails userDetails = new User(credentials.getUsername(), credentials.getPassword(), Arrays.asList(new SimpleGrantedAuthority("my-role")));
        
        return credentials;
    }
}
