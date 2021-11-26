package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByName(String name);
    Boolean existsUserByName(String username);
    Boolean existsUserByMail(String email);
}