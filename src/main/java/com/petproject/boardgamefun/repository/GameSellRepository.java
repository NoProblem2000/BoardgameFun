package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.Game;
import com.petproject.boardgamefun.model.GameSell;
import com.petproject.boardgamefun.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSellRepository extends JpaRepository<GameSell, Integer> {
    GameSell findByGameAndUser(Game game, User user);
}