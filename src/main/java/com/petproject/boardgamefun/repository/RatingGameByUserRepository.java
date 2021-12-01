package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.Game;
import com.petproject.boardgamefun.model.RatingGameByUser;
import com.petproject.boardgamefun.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingGameByUserRepository extends JpaRepository<RatingGameByUser, Integer> {
    RatingGameByUser findByGameAndUser(Game game, User user);
}