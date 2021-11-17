package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Integer> {
}