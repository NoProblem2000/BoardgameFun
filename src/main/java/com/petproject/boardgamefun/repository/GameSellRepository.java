package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.GameSell;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSellRepository extends JpaRepository<GameSell, Integer> {
    GameSell findGameSell_ByUserIdAndGameId( Integer userId, Integer gameId);
}