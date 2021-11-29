package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.Game;
import com.petproject.boardgamefun.model.User;
import com.petproject.boardgamefun.model.UserOwnGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOwnGameRepository extends JpaRepository<UserOwnGame, Integer> {
    UserOwnGame findByGameAndUser(Game game, User user);
}