package com.petproject.boardgamefun;

import com.petproject.boardgamefun.security.services.UserDetailsImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;
import java.util.List;

@TestConfiguration
public class SpringSecurityWebTestConfig {
    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        UserDetailsImpl basicUser = new UserDetailsImpl(1,"Basic User", "user@company.com", "password", List.of(
                new SimpleGrantedAuthority("ROLE_USER")));


        UserDetailsImpl managerUser = new UserDetailsImpl(2,"Admin", "admin@company.com", "adminPassword", List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN")));

        return new InMemoryUserDetailsManager(Arrays.asList(
                basicUser, managerUser
        ));
    }
}
