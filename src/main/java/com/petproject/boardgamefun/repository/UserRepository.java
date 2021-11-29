package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserById(Integer id);
    User findUserByName(String name);
    Boolean existsByName(String username);
    Boolean existsByMail(String email);
}