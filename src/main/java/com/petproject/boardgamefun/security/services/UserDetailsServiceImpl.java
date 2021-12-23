package com.petproject.boardgamefun.security.services;

import com.petproject.boardgamefun.model.User;
import com.petproject.boardgamefun.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }



    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findUserByName(userName);
        if(user == null)
            throw new UsernameNotFoundException("User Not Found with username: " + userName);

        return UserDetailsImpl.build(user);
    }
}
