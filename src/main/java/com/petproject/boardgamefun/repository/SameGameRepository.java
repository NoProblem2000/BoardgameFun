package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.SameGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SameGameRepository extends JpaRepository<SameGame, Integer> {
}